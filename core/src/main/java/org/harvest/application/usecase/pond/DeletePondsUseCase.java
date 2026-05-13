package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeletePondsCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.inbound.BaseUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

public class DeletePondsUseCase extends AuthenticationUseCase<DeletePondsCommand, DefaultResult> {
    private final PondsRepository pondsRepository;

    public DeletePondsUseCase(PondsRepository pondsRepository) {
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(DeletePondsCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeletePondsCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        pondsRepository.delete(ponds);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeletePondsCommand command) {
        if (command.pondsId() == null){
            throw new ValidationException("Ponds Id cannot be null");
        }
    }
}
