package org.harvest.application.usecase.death;

import org.harvest.application.dto.command.DeleteDeathRecordCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DeathRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DeathRecord;
import org.harvest.shared.exception.ValidationException;

public class DeleteDeathRecordUseCase extends AuthenticationUseCase<DeleteDeathRecordCommand, DefaultResult> {

    private final DeathRecordRepository deathRecordRepository;

    public DeleteDeathRecordUseCase(DeathRecordRepository deathRecordRepository) {
        this.deathRecordRepository = deathRecordRepository;
    }

    @Override
    protected UserSession authenticate(DeleteDeathRecordCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteDeathRecordCommand command, UserSession userSession) {
        DeathRecord deathRecord = deathRecordRepository.findById(command.deathRecordId());
        if (deathRecord == null){
            throw new ValidationException("DeathRecord not found");
        }
        deathRecordRepository.delete(deathRecord);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteDeathRecordCommand command) {
        if (command.deathRecordId() == null){
            throw new ValidationException("DeathRecord Id cannot be null");
        }

    }
}
