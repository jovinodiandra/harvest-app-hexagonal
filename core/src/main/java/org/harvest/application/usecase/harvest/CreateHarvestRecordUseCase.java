package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateHarvestRecordCommand;
import org.harvest.application.dto.result.CreateHarvestRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateHarvestRecordUseCase extends AuthenticationUseCase<CreateHarvestRecordCommand, CreateHarvestRecordResult> {
    private final HarvestRecordRepository harvestRecordRepository;
    private final PondsRepository pondsRepository;

    public CreateHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository, PondsRepository pondsRepository) {
        this.harvestRecordRepository = harvestRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreateHarvestRecordCommand command) {
        return command.session();
    }

    @Override
    protected CreateHarvestRecordResult executeBusiness(CreateHarvestRecordCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondId());
        if (ponds == null) {
            throw new ValidationException("Ponds not found");
        }
        HarvestRecord harvestRecord = new HarvestRecord(harvestRecordRepository.nextId(),ponds.name() ,command.pondId(), command.harvestDate(), userSession.getOrganizationId(), command.harvestFishCount(), command.totalWeight(), command.notes());
        harvestRecordRepository.save(harvestRecord);
        return new CreateHarvestRecordResult(harvestRecord.id(),harvestRecord.pondsId(), command.harvestDate(), command.harvestFishCount(),command.totalWeight(),command.notes(),LocalDateTime.now());
    }

    @Override
    protected void validateCommand(CreateHarvestRecordCommand command) {
        if (command.pondId() == null) {
            throw new ValidationException("Pond Id cannot be null");
        }
        if (command.harvestDate() == null || command.harvestDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Harvest Date cannot be in the future");
        }
        if (command.harvestFishCount() < 0){
            throw new ValidationException("Harvest Fish Count must be greater than or equal to 0");
        }

        if (command.totalWeight().compareTo(BigDecimal.ZERO) < 0){
            throw new ValidationException("Total Weight must be greater than or equal to 0");
        }

    }
}
