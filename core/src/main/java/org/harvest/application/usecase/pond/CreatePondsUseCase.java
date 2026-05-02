package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreatePondsCommand;
import org.harvest.application.dto.result.CreatePondsResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class CreatePondsUseCase extends AuthenticationUseCase<CreatePondsCommand, CreatePondsResult> {
    private final PondsRepository pondsRepository;

    public CreatePondsUseCase(PondsRepository pondsRepository) {
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreatePondsCommand command) {

        return command.session();
    }

    @Override
    protected CreatePondsResult executeBusiness(CreatePondsCommand command, UserSession userSession) {
        Ponds ponds = new Ponds(pondsRepository.nextId(), command.name(), command.capacity(), command.location(), userSession.getOrganizationId());
        pondsRepository.save(ponds);
        return new CreatePondsResult(ponds.id(), command.name(), command.location(), command.capacity(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(CreatePondsCommand command) {
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
