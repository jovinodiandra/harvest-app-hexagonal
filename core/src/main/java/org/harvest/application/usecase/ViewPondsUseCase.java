package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewPondsQuery;
import org.harvest.application.dto.result.ViewPondsResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.inbound.BaseUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

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
        int offset = (command.page()-1 )* command.limit();
        List<Ponds> ponds = pondsRepository.findAllByOrganizationId(userSession.getOrganizationId(),offset,command.limit());
        return new ViewPondsResult(ponds);
    }

    @Override
    protected void validateCommand(ViewPondsQuery command) {
        if (command.page() < 1){
            throw new ValidationException("Page must be greater than 0");
        }

        if (command.limit() < 1){
            throw new ValidationException("Limit must be greater than 0");
        }
    }
}
