package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewSeedQuery;
import org.harvest.application.dto.result.ViewSeedResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SeedRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Seed;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class VIewSeedUseCase extends AuthenticationUseCase<ViewSeedQuery, ViewSeedResult> {
    private final SeedRepository seedRepository;

    public VIewSeedUseCase(SeedRepository seedRepository) {
        this.seedRepository = seedRepository;
    }


    @Override
    protected UserSession authenticate(ViewSeedQuery command) {
        return command.session();
    }

    @Override
    protected ViewSeedResult executeBusiness(ViewSeedQuery command, UserSession userSession) {
        int offset = (command.page() -1)* command.limit();
        List<Seed> seed = seedRepository.findAllByOrganization(userSession.getOrganizationId(),offset, command.limit());
        return new ViewSeedResult(seed);
    }

    @Override
    protected void validateCommand(ViewSeedQuery command) {
        if (command.page() < 1){
            throw new ValidationException("Page must be greater than 0");
        }
        if (command.limit() < 1){
            throw new ValidationException("Limit must be greater than 0");
        }

    }
}
