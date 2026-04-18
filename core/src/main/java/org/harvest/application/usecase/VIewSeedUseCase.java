package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewSeedQuery;
import org.harvest.application.dto.result.ViewSeedResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SeedRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Seed;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

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
        Pagination pagination = new Pagination(command.page(), command.limit());
        List<Seed> seed = seedRepository.findAllByOrganization(userSession.getOrganizationId(),pagination);
        return new ViewSeedResult(seed);
    }

    @Override
    protected void validateCommand(ViewSeedQuery command) {


    }
}
