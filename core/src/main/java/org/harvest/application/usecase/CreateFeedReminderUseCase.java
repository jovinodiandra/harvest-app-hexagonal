package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateFeedReminderCommand;
import org.harvest.application.dto.result.CreateFeedReminderResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedReminderRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedReminder;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.NotFoundException;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class CreateFeedReminderUseCase extends AuthenticationUseCase<CreateFeedReminderCommand, CreateFeedReminderResult> {

    private final FeedReminderRepository feedReminderRepository;
    private final PondsRepository pondsRepository;

    public CreateFeedReminderUseCase(FeedReminderRepository feedReminderRepository, PondsRepository pondsRepository) {
        this.feedReminderRepository = feedReminderRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreateFeedReminderCommand command) {
        return command.session();
    }

    @Override
    protected CreateFeedReminderResult executeBusiness(CreateFeedReminderCommand command, UserSession userSession) {
        Ponds pond = pondsRepository.findById(command.pondId());
        if (pond == null || !pond.organizationId().equals(userSession.getOrganizationId())) {
            throw new NotFoundException("Kolam tidak ditemukan");
        }

        FeedReminder feedReminder = new FeedReminder(
                feedReminderRepository.nextId(),
                command.pondId(),
                command.reminderDate(),
                command.reminderTime(),
                command.feedType(),
                command.notes(),
                userSession.getOrganizationId()
        );

        feedReminderRepository.save(feedReminder);

        return new CreateFeedReminderResult(
                feedReminder.id(),
                feedReminder.pondsId(),
                feedReminder.reminderDate(),
                feedReminder.reminderTime(),
                feedReminder.feedType(),
                feedReminder.notes(),
                LocalDateTime.now() // Asumsi created_at adalah saat ini
        );
    }

    @Override
    protected void validateCommand(CreateFeedReminderCommand command) {
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
