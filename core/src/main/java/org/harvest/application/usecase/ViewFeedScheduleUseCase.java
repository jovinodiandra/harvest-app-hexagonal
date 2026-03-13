package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFeedScheduleQuery;
import org.harvest.application.dto.result.ViewFeedScheduleResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedScheduleRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedSchedule;
import org.harvest.shared.exception.ValidationException;

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
        int offset = (command.page() - 1) * command.limit();
        List<FeedSchedule> feedSchedules = feedScheduleRepository.findByOrganizationId(userSession.getOrganizationId(), offset, command.limit());
        return new ViewFeedScheduleResult(feedSchedules);
    }

    @Override
    protected void validateCommand(ViewFeedScheduleQuery command) {
        if (command.page() < 1) {
            throw new ValidationException("Page must be greater than 0");
        }

        if (command.limit() < 1) {
            throw new ValidationException("Limit must be greater than 0");
        }
    }
}
