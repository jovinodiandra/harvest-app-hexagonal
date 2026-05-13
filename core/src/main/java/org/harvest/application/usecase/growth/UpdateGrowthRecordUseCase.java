package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateGrowthRecordCommand;
import org.harvest.application.dto.result.UpdateGrowthRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.GrowthRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.GrowthRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateGrowthRecordUseCase extends AuthenticationUseCase<UpdateGrowthRecordCommand, UpdateGrowthRecordResult> {

    private final GrowthRecordRepository growthRecordRepository;
    private final PondsRepository pondsRepository;

    public UpdateGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository, PondsRepository pondsRepository) {
        this.growthRecordRepository = growthRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateGrowthRecordCommand command) {
        return command.session();
    }

    @Override
    protected UpdateGrowthRecordResult executeBusiness(UpdateGrowthRecordCommand command, UserSession userSession) {
        GrowthRecord growthRecord = growthRecordRepository.findById(command.growthRecordId());
        if (growthRecord == null) {
            throw new ValidationException("GrowthRecord not found");
        }
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null) {
            throw new ValidationException("Ponds not found");
        }
        GrowthRecord updateGrowthRecord = growthRecord.update(command.recordDate(), command.averageLength(), command.averageWeight());
        growthRecordRepository.update(updateGrowthRecord);
        return new UpdateGrowthRecordResult(growthRecord.id(), growthRecord.pondsId(), ponds.name(), command.recordDate(), command.averageLength(), command.averageWeight());
    }

    @Override
    protected void validateCommand(UpdateGrowthRecordCommand command) {

        if (command.pondsId() == null) {
            throw new ValidationException("Ponds Id cannot be null");
        }

        if (command.recordDate() == null) {
            throw new ValidationException("Record Date cannot be null");
        }

        if (command.recordDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Record Date cannot be in the future");
        }


        if (command.averageWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Average Weight cannot be null and must be greater than 0");
        }
        if (command.averageLength().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Average Length cannot be null and must be greater than 0");
        }
    }
}
