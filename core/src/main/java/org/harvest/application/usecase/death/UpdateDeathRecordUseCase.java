package org.harvest.application.usecase.death;

import org.harvest.application.dto.command.UpdateDeathRecordCommand;
import org.harvest.application.dto.result.UpdateDeathRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DeathRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DeathRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;

public class UpdateDeathRecordUseCase extends AuthenticationUseCase<UpdateDeathRecordCommand, UpdateDeathRecordResult> {

    private final DeathRecordRepository deathRecordRepository;
    private final PondsRepository pondsRepository;

    public UpdateDeathRecordUseCase(DeathRecordRepository deathRecordRepository, PondsRepository pondsRepository) {
        this.deathRecordRepository = deathRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateDeathRecordCommand command) {
        return command.session();
    }

    @Override
    protected UpdateDeathRecordResult executeBusiness(UpdateDeathRecordCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null) {
            throw new ValidationException("Ponds not found");
        }
        DeathRecord deathRecord = deathRecordRepository.findById(command.deathRecordId());
        if (deathRecord == null) {
            throw new ValidationException("DeathRecord not found");
        }
        deathRecordRepository.update(deathRecord.update(command.recordDate(), command.deathCount(), command.notes()));
        return new UpdateDeathRecordResult(deathRecord.id(), deathRecord.pondsId(), ponds.name(), command.recordDate(), command.deathCount(), command.notes());
    }

    @Override
    protected void validateCommand(UpdateDeathRecordCommand command) {

        if (command.pondsId() == null) {
            throw new ValidationException("Ponds Id cannot be null");
        }

        if (command.deathRecordId() == null) {
            throw new ValidationException("DeathRecord Id cannot be null");
        }
        if (command.deathCount() < 1) {
            throw new ValidationException("Death Count must be greater than 0");
        }
        if (command.recordDate() == null|| command.recordDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid record date");
        }

    }
}
