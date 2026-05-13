package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFeedPriceQuery;
import org.harvest.application.dto.result.ViewFeedPriceResult;
import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.application.dto.value.Role;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedPriceRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.shared.query.Pagination;
import org.harvest.domain.FeedPrice;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class ViewFeedPriceUseCase extends AuthenticationUseCase<ViewFeedPriceQuery, ViewFeedPriceResult> {
    private final FeedPriceRepository feedPriceRepository;

    public ViewFeedPriceUseCase(FeedPriceRepository feedPriceRepository) {
        this.feedPriceRepository = feedPriceRepository;
    }

    @Override
    protected UserSession authenticate(ViewFeedPriceQuery command) {
        return command.session();
    }

    @Override
    protected ViewFeedPriceResult executeBusiness(ViewFeedPriceQuery query, UserSession userSession) {
        if (!(userSession.getRole() == Role.OWNER || userSession.getRole() == Role.ADMIN)) {
            throw new ValidationException("role must be owner and admin");
        }
        Pagination pagination = new Pagination(query.page(), query.limit());
        List<FeedPrice> feedPriceList = feedPriceRepository.findAllByOrganizationId(userSession.getOrganizationId(), pagination);
        return new ViewFeedPriceResult(feedPriceList);
    }

    @Override
    protected void validateCommand(ViewFeedPriceQuery command) {
        if (command.status() != null &&
                command.status() != FeedPriceStatus.ACTIVE &&
                command.status() != FeedPriceStatus.NONACTIVE) {
            throw new ValidationException("invalid status");
        }
    }
}
