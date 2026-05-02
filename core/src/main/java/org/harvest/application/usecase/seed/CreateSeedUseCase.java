package org.harvest.application.usecase.seed;

import org.harvest.application.dto.command.CreateSeedCommand;
import org.harvest.application.dto.result.CreateSeedResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SeedRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Seed;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateSeedUseCase extends AuthenticationUseCase<CreateSeedCommand, CreateSeedResult> {
    private final SeedRepository seedRepository;

    public CreateSeedUseCase(SeedRepository seedRepository) {
        this.seedRepository = seedRepository;
    }

    @Override
    protected UserSession authenticate(CreateSeedCommand command) {
        return command.session();
    }

    @Override
    protected CreateSeedResult executeBusiness(CreateSeedCommand command, UserSession userSession) {
        Seed seed = new Seed(seedRepository.nextId(), command.pondsId(),command.type(), command.quantity(),  command.stockDate(),userSession.getOrganizationId());
        seedRepository.save(seed);
        return new CreateSeedResult(seed.id(),seed.pondsId(),seed.type(),seed.quantity(), seed.stockDate(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(CreateSeedCommand command) {
        if (command.pondsId() == null) {
            throw new ValidationException("Ponds Id cannot be null");

        }
        if (command.quantity() < 1) {
            throw new ValidationException("Quantity must be greater than 0");
        }
        if (command.type() == null || command.type().isBlank()) {
            throw new ValidationException("Seed Type cannot be empty ");
        }

        if (command.stockDate() == null) {
            throw new ValidationException("Stock Date cannot be null"); // tanggal tebar bibit
        }

        if (command.stockDate().isAfter(LocalDate.now())){
            throw new ValidationException("Stock Date cannot be in the future");
        }
    }
}
