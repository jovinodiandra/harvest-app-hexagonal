package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewWatterQualityQuery;
import org.harvest.application.dto.result.ViewWatterQualityResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.WatterQualityRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.domain.WatterQuality;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

import java.util.List;

public class ViewWatterQualityUseCase extends AuthenticationUseCase<ViewWatterQualityQuery, ViewWatterQualityResult> {
    private final WatterQualityRepository watterQualityRepository;
    private final PondsRepository pondsRepository;


    public ViewWatterQualityUseCase(WatterQualityRepository watterQualityRepository, PondsRepository pondsRepository) {
        this.watterQualityRepository = watterQualityRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(ViewWatterQualityQuery command) {
        return command.session();
    }

    @Override
    protected ViewWatterQualityResult executeBusiness(ViewWatterQualityQuery command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null) {
            throw new ValidationException("Ponds not found");
        }
        Pagination pagination = new Pagination(command.page(), command.limit());
        List<WatterQuality> watterQualityList = watterQualityRepository.findAllByPondsId(command.pondsId(), pagination);
        return new ViewWatterQualityResult(watterQualityList);
    }

    @Override
    protected void validateCommand(ViewWatterQualityQuery command) {

    }
}
