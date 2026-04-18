package org.harvest.application.port.service;

import org.harvest.domain.FeedReminder;
import org.harvest.domain.HarvestReminder;
import org.harvest.domain.Ponds;

import java.util.UUID;

public interface ReminderSchedulerService {

    void scheduleFeedReminder(FeedReminder reminder, Ponds pond);

    void scheduleHarvestReminder(HarvestReminder reminder, Ponds pond);

    void updateFeedReminder(FeedReminder reminder, Ponds pond);

    void updateHarvestReminder(HarvestReminder reminder, Ponds pond);

    void cancelFeedReminder(UUID reminderId);

    void cancelHarvestReminder(UUID reminderId);

}
