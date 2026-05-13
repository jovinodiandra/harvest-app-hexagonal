package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFeedScheduleQuery;
import org.harvest.application.dto.result.ViewFeedScheduleResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedScheduleRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedSchedule;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

import java.util.List;

public class ViewFeedScheduleUseCase extends AuthenticationUseCase<ViewFeedScheduleQuery, ViewFeedScheduleResult> {
    private final FeedScheduleRepository feedScheduleRepository;

    public ViewFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository) {
        this.feedScheduleRepository = feedScheduleRepository;
    }

    @Override
    protected UserSession authenticate(ViewFeedScheduleQuery command) {
        return command.session();
    }

    @Override
    protected ViewFeedScheduleResult executeBusiness(ViewFeedScheduleQuery command, UserSession userSession) {
        Pagination pagination = new Pagination(command.page(), command.limit());
        List<FeedSchedule> feedSchedules = feedScheduleRepository.findByOrganizationId(userSession.getOrganizationId(), pagination);
        return new ViewFeedScheduleResult(feedSchedules);
    }

    @Override
    protected void validateCommand(ViewFeedScheduleQuery command) {

    }
}
