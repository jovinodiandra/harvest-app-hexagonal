package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteFeedReminderCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedReminderRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedReminder;
import org.harvest.shared.exception.ValidationException;

public class DeleteFeedReminderUseCase extends AuthenticationUseCase<DeleteFeedReminderCommand, DefaultResult> {
    private final FeedReminderRepository feedReminderRepository;

    public DeleteFeedReminderUseCase(FeedReminderRepository feedReminderRepository) {
        this.feedReminderRepository = feedReminderRepository;

    }

    @Override
    protected UserSession authenticate(DeleteFeedReminderCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteFeedReminderCommand command, UserSession userSession) {
        FeedReminder feedReminder = feedReminderRepository.findById(command.feedReminderId());
        if (feedReminder == null) {
            throw new ValidationException("Feed Reminder not found");
        }
        feedReminderRepository.delete(feedReminder);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteFeedReminderCommand command) {

        if (command.feedReminderId() == null) {
            throw new ValidationException("Feed Reminder Id Cannot be null");
        }
    }
}
