package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateFeedReminderCommand;
import org.harvest.application.dto.result.UpdateFeedReminderResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedReminderRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedReminder;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.NotFoundException;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class UpdateFeedReminderUseCase extends AuthenticationUseCase<UpdateFeedReminderCommand, UpdateFeedReminderResult> {

    private final FeedReminderRepository feedReminderRepository;
    private final PondsRepository pondsRepository;

    public UpdateFeedReminderUseCase(FeedReminderRepository feedReminderRepository, PondsRepository pondsRepository) {
        this.feedReminderRepository = feedReminderRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateFeedReminderCommand command) {
        return command.session();
    }

    @Override
    protected UpdateFeedReminderResult executeBusiness(UpdateFeedReminderCommand command, UserSession userSession) {
        FeedReminder feedReminder = feedReminderRepository.findById(command.id());
        if (feedReminder == null || !feedReminder.organizationId().equals(userSession.getOrganizationId())) {
            throw new NotFoundException("Reminder pakan tidak ditemukan");
        }

        Ponds pond = pondsRepository.findById(command.pondId());
        if (pond == null || !pond.organizationId().equals(userSession.getOrganizationId())) {
            throw new NotFoundException("Kolam tidak ditemukan");
        }

        feedReminder = feedReminder.update(
                command.pondId(),
                command.reminderDate(),
                command.reminderTime(),
                command.feedType(),
                command.notes()
        );

        feedReminderRepository.update(feedReminder);

        return new UpdateFeedReminderResult(
                feedReminder.id(),
                feedReminder.pondsId(),
                feedReminder.reminderDate(),
                feedReminder.reminderTime(),
                feedReminder.feedType(),
                feedReminder.notes(),
                LocalDateTime.now()
        );
    }

    @Override
    protected void validateCommand(UpdateFeedReminderCommand command) {
        if (command.id() == null) {
            throw new ValidationException("Data yang dikirim tidak valid");
        }
        if (command.pondId() == null) {
            throw new ValidationException("Data yang dikirim tidak valid");
        }
        if (command.reminderDate() == null) {
            throw new ValidationException("Data yang dikirim tidak valid");
        }
        if (command.reminderTime() == null) {
            throw new ValidationException("Data yang dikirim tidak valid");
        }
    }
}
