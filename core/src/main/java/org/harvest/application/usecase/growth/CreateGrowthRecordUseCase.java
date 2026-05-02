package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateGrowthRecordCommand;
import org.harvest.application.dto.result.CreateGrowthRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.GrowthRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.GrowthRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;

public class CreateGrowthRecordUseCase extends AuthenticationUseCase<CreateGrowthRecordCommand, CreateGrowthRecordResult> {
    private final GrowthRecordRepository growthRecordRepository;
    private final PondsRepository pondsRepository;

    public CreateGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository, PondsRepository pondsRepository) {
        this.growthRecordRepository = growthRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreateGrowthRecordCommand command) {
        return command.session();
    }

    @Override
    protected CreateGrowthRecordResult executeBusiness(CreateGrowthRecordCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        // GrowthRecord constructor expects (averageLength, averageWeight)
        GrowthRecord growthRecord = new GrowthRecord(
                growthRecordRepository.nextId(),
                command.pondsId(),
                command.recordDate(),
                command.averageLength(),
                command.averageWeight(),
                userSession.getOrganizationId()
        );
        growthRecordRepository.save(growthRecord);
        return new CreateGrowthRecordResult(growthRecord.id(), growthRecord.pondsId(), ponds.name(), command.recordDate(), command.averageLength(), command.averageWeight());
    }

    @Override
    protected void validateCommand(CreateGrowthRecordCommand command) {
        if (command.pondsId() == null) {
            throw new ValidationException("Ponds Id Cannot be null");
        }
        if (command.recordDate() == null) {
            throw new ValidationException("Record Date Cannot be null");
        }

        if (command.averageLength() == null || command.averageLength().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Average Length Cannot be null and must be greater than 0");
        }
        if (command.averageWeight() == null || command.averageWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Average Weight Cannot be null and must be greater than 0");
        }

    }
}
