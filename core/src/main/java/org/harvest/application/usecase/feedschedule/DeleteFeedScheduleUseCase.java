package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteFeedScheduleCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedScheduleRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedSchedule;
import org.harvest.shared.exception.ValidationException;

public class DeleteFeedScheduleUseCase extends AuthenticationUseCase<DeleteFeedScheduleCommand, DefaultResult> {
    private final FeedScheduleRepository feedScheduleRepository;

    public DeleteFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository) {
        this.feedScheduleRepository = feedScheduleRepository;
    }

    @Override
    protected UserSession authenticate(DeleteFeedScheduleCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteFeedScheduleCommand command, UserSession userSession) {
        FeedSchedule feedSchedule = feedScheduleRepository.findById(command.feedScheduleId());
        if(feedSchedule == null){
            throw new ValidationException("Feed Schedule not found");
        }
        feedScheduleRepository.delete(feedSchedule);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteFeedScheduleCommand command) {

        if (command.feedScheduleId() == null){
            throw new ValidationException("Feed Schedule Id cannot be null");
        }
    }
}
