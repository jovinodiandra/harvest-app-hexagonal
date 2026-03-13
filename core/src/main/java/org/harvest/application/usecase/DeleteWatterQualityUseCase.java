package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteWatterQualityCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.WatterQualityRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.WatterQuality;
import org.harvest.shared.exception.ValidationException;

public class DeleteWatterQualityUseCase extends AuthenticationUseCase<DeleteWatterQualityCommand, DefaultResult> {
    private final WatterQualityRepository watterQualityRepository;

    public DeleteWatterQualityUseCase(WatterQualityRepository watterQualityRepository) {
        this.watterQualityRepository = watterQualityRepository;
    }

    @Override
    protected UserSession authenticate(DeleteWatterQualityCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteWatterQualityCommand command, UserSession userSession) {
        WatterQuality watterQuality = watterQualityRepository.findById(command.watterQualityId());
        if (watterQuality == null) {
            throw new ValidationException("WatterQuality not found");
        }

        watterQualityRepository.delete(watterQuality);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteWatterQualityCommand command) {
        if (command.watterQualityId() == null) {
            throw new ValidationException("WatterQuality Id cannot be null");
        }
    }
}
