package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteSeedCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SeedRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Seed;
import org.harvest.shared.exception.ValidationException;

public class DeleteSeedUseCase extends AuthenticationUseCase<DeleteSeedCommand, DefaultResult> {
    private final SeedRepository seedRepository;

    public DeleteSeedUseCase(SeedRepository seedRepository) {
        this.seedRepository = seedRepository;
    }

    @Override
    protected UserSession authenticate(DeleteSeedCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteSeedCommand command, UserSession userSession) {
        Seed seed = seedRepository.findById(command.seedId());
        if (seed == null){
            throw new ValidationException("seed not found");
        }
        seedRepository.delete(seed);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteSeedCommand command) {
        if (command.seedId() == null){
            throw new ValidationException("Seed Id cannot be null");

        }

    }
}
