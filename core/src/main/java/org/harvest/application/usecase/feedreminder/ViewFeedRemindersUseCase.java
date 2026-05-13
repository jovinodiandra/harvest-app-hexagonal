package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFeedRemindersQuery;
import org.harvest.application.dto.result.FeedReminderItem;
import org.harvest.application.dto.result.ViewFeedRemindersResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedReminderRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedReminder;
import org.harvest.domain.Ponds;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ViewFeedRemindersUseCase extends AuthenticationUseCase<ViewFeedRemindersQuery, ViewFeedRemindersResult> {

    private final FeedReminderRepository feedReminderRepository;
    private final PondsRepository pondsRepository;

    public ViewFeedRemindersUseCase(FeedReminderRepository feedReminderRepository, PondsRepository pondsRepository) {
        this.feedReminderRepository = feedReminderRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(ViewFeedRemindersQuery command) {
        return command.session();
    }

    @Override
    protected ViewFeedRemindersResult executeBusiness(ViewFeedRemindersQuery command, UserSession userSession) {
        List<FeedReminder> reminders = feedReminderRepository.findByFilters(
                userSession.getOrganizationId(),
                command.pondId(),
                command.date()
        );

        List<FeedReminderItem> items = reminders.stream().map(reminder -> {
            Ponds pond = pondsRepository.findById(reminder.pondsId());
            String pondName = (pond != null) ? pond.name() : "Unknown";
            return new FeedReminderItem(
                    reminder.id(),
                    reminder.pondsId(),
                    pondName,
                    reminder.reminderDate(),
                    reminder.reminderTime(),
                    reminder.feedType(),
                    reminder.notes(),
                    LocalDateTime.now() // Placeholder as FeedReminder doesn't have createdAt
            );
        }).collect(Collectors.toList());

        return new ViewFeedRemindersResult(items);
    }

    @Override
    protected void validateCommand(ViewFeedRemindersQuery command) {
        // Optional filters, so no mandatory validation needed here.
    }
}
