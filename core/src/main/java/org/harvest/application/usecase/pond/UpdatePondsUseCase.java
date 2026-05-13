package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdatePondsCommand;
import org.harvest.application.dto.result.UpdatePondsResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class UpdatePondsUseCase extends AuthenticationUseCase<UpdatePondsCommand, UpdatePondsResult> {
    private final PondsRepository pondsRepository;

    public UpdatePondsUseCase(PondsRepository pondsRepository) {
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdatePondsCommand command) {
        return command.session();
    }

    @Override
    protected UpdatePondsResult executeBusiness(UpdatePondsCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        Ponds updatePonds = ponds.update(command.name(), command.capacity(),command.location());
        pondsRepository.update(updatePonds);
        return new UpdatePondsResult(ponds.id(), command.name(), command.location(), command.capacity(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(UpdatePondsCommand command) {
        if (command.name() == null){
            throw new ValidationException("Name cannot be null");
        }

        if (command.location() == null) {
            throw new ValidationException("Location cannot be null");
        }

        if (command.capacity() < 1){
            throw new ValidationException("Capacity must be greater than 0");
        }
    }
}
