package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewPondsQuery;
import org.harvest.application.dto.result.ViewPondsResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.inbound.BaseUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

import java.util.List;

public class ViewPondsUseCase extends AuthenticationUseCase<ViewPondsQuery, ViewPondsResult> {
    private final PondsRepository pondsRepository;

    public ViewPondsUseCase(PondsRepository pondsRepository) {
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(ViewPondsQuery command) {
        return command.session();
    }

    @Override
    protected ViewPondsResult executeBusiness(ViewPondsQuery command, UserSession userSession) {
        Pagination pagination = new Pagination(command.page(),command.limit());
        List<Ponds> ponds = pondsRepository.findAllByOrganizationId(userSession.getOrganizationId(),pagination);
        return new ViewPondsResult(ponds);
    }

    @Override
    protected void validateCommand(ViewPondsQuery command) {

    }
}
