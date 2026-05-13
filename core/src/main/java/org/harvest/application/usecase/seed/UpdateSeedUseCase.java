package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateSeedCommand;
import org.harvest.application.dto.result.UpdateSeedResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.SeedRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.domain.Seed;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;

public class UpdateSeedUseCase extends AuthenticationUseCase<UpdateSeedCommand, UpdateSeedResult> {
    private final SeedRepository seedRepository;
    private final PondsRepository pondsRepository;

    public UpdateSeedUseCase(SeedRepository seedRepository, PondsRepository pondsRepository) {
        this.seedRepository = seedRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateSeedCommand command) {
        return command.session();
    }

    @Override
    protected UpdateSeedResult executeBusiness(UpdateSeedCommand command, UserSession userSession) {
        Seed seed = seedRepository.findById(command.seedId());
        if (seed == null){
            throw new ValidationException("Seed not found");
        }
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        Seed updateSeed = seed.update(command.type(), command.quantity(), command.stockDate());
        seedRepository.Update(updateSeed);
        return new UpdateSeedResult(seed.id(),seed.pondsId(), ponds.name() ,command.type(),command.quantity(), LocalDate.now());
    }

    @Override
    protected void validateCommand(UpdateSeedCommand command) {

        if (command.pondsId() == null) {
            throw new ValidationException("Ponds Id cannot be null");
        }
        if (command.type() == null || command.type().isBlank()) {
            throw new ValidationException("Seed Type cannot be empty");
        }
        if (command.quantity() < 1) {
            throw new ValidationException("Quantity must be greater than 0");
        }
        if (command.stockDate() == null ) {
            throw new ValidationException("Stock Date cannot be null");
        }

        if (command.stockDate().isAfter(LocalDate.now())){
            throw new ValidationException("Stock Date cannot be in the future");
        }
    }
}
