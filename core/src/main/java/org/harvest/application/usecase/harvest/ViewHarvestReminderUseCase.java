package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewHarvestReminderQuery;
import org.harvest.application.dto.result.FeedReminderItem;
import org.harvest.application.dto.result.HarvestReminderItem;
import org.harvest.application.dto.result.ViewHarvestReminderResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestReminderRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestReminder;
import org.harvest.domain.Ponds;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ViewHarvestReminderUseCase extends AuthenticationUseCase<ViewHarvestReminderQuery, ViewHarvestReminderResult> {
    private final HarvestReminderRepository harvestReminderRepository;
    private final PondsRepository pondsRepository;

    public ViewHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository, PondsRepository pondsRepository) {
        this.harvestReminderRepository = harvestReminderRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(ViewHarvestReminderQuery command) {
        return command.session();
    }

    @Override
    protected ViewHarvestReminderResult executeBusiness(ViewHarvestReminderQuery command, UserSession userSession) {
        List<HarvestReminder> harvestReminders = harvestReminderRepository.findByFilters(userSession.getOrganizationId(),command.pondId(),command.date());

        List<HarvestReminderItem> items = harvestReminders.stream().map(reminder -> {
            Ponds pond = pondsRepository.findById(reminder.pondId());
            String pondName = (pond != null) ? pond.name() : "Unknown";
            return new HarvestReminderItem(
                    reminder.id(),
                    reminder.pondId(),
                    pondName,
                    reminder.reminderDate(),
                    reminder.reminderTime(),
                    reminder.notes(),
                    LocalDateTime.now() // Placeholder as FeedReminder doesn't have createdAt
            );
        }).collect(Collectors.toList());
        return new ViewHarvestReminderResult(items);
    }

    @Override
    protected void validateCommand(ViewHarvestReminderQuery command) {

    }
}
