package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateDeathRecordCommand;
import org.harvest.application.dto.result.CreateDeathRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DeathRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DeathRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;

public class CreateDeathRecordUseCase extends AuthenticationUseCase<CreateDeathRecordCommand, CreateDeathRecordResult> {
    private final DeathRecordRepository deathRecordRepository;
    private final PondsRepository pondsRepository;

    public CreateDeathRecordUseCase(DeathRecordRepository deathRecordRepository, PondsRepository pondsRepository) {
        this.deathRecordRepository = deathRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreateDeathRecordCommand command) {
        return command.session();
    }

    @Override
    protected CreateDeathRecordResult executeBusiness(CreateDeathRecordCommand command, UserSession userSession) {
        DeathRecord deathRecord = new DeathRecord(deathRecordRepository.nextId(), command.pondsId(),command.recordDate(), command.deathCount() , command.notes(), userSession.getOrganizationId());
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        deathRecordRepository.save(deathRecord);
        return new CreateDeathRecordResult(deathRecord.id(), deathRecord.pondsId(),ponds.name(), deathRecord.recordDate(), deathRecord.deathCount(), deathRecord.notes() );
    }

    @Override
    protected void validateCommand(CreateDeathRecordCommand command) {
            if (command.pondsId() == null) {
                throw new ValidationException("Ponds Id cannot be null");
            }
            if (command.recordDate() == null || command.recordDate().isAfter(LocalDate.now())){
                throw new ValidationException("invalid record date");
            }
            if (command.deathCount() < 1){
                throw new ValidationException("Death Count must be greater than 0");
            }
    }
}
