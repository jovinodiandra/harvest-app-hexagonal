package org.harvest.reminder.adapter;

import org.harvest.application.port.outbound.LogManager;
import org.harvest.application.port.outbound.NotificationSender;
import org.harvest.application.port.service.ReminderSchedulerService;
import org.harvest.domain.FeedReminder;
import org.harvest.domain.HarvestReminder;
import org.harvest.domain.LogCategory;
import org.harvest.domain.Notification;
import org.harvest.domain.Ponds;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Simple thread-based reminder scheduler adapter.
 * Uses ScheduledExecutorService for scheduling reminders.
 */
public class SimpleThreadReminderSchedulerAdapter implements ReminderSchedulerService {

    private static final String SOURCE = "SimpleThreadReminderSchedulerAdapter";

    private final ScheduledExecutorService scheduler;
    private final NotificationSender notificationSender;
    private final LogManager logManager;

    private final Map<UUID, ScheduledFuture<?>> feedReminderTasks = new ConcurrentHashMap<>();
    private final Map<UUID, ScheduledFuture<?>> harvestReminderTasks = new ConcurrentHashMap<>();

    public SimpleThreadReminderSchedulerAdapter(
            NotificationSender notificationSender,
            LogManager logManager
    ) {
        this(notificationSender, logManager, Executors.newScheduledThreadPool(2));
    }

    public SimpleThreadReminderSchedulerAdapter(
            NotificationSender notificationSender,
            LogManager logManager,
            ScheduledExecutorService scheduler
    ) {
        this.notificationSender = notificationSender;
        this.logManager = logManager;
        this.scheduler = scheduler;
    }

    @Override
    public void scheduleFeedReminder(FeedReminder reminder, Ponds pond) {
        cancelFeedReminder(reminder.id());

        long delayMillis = calculateDelay(reminder.reminderDate().atTime(reminder.reminderTime()));

        if (delayMillis <= 0) {
            logManager.warn(LogCategory.SCHEDULER,
                    "Feed reminder time has passed, skipping: " + reminder.id(),
                    SOURCE);
            return;
        }

        ScheduledFuture<?> task = scheduler.schedule(
                () -> executeFeedReminder(reminder, pond),
                delayMillis,
                TimeUnit.MILLISECONDS
        );

        feedReminderTasks.put(reminder.id(), task);

        logManager.info(LogCategory.SCHEDULER,
                "Scheduled feed reminder: " + reminder.id() + " for pond: " + pond.name(),
                SOURCE,
                Map.of(
                        "reminderId", reminder.id().toString(),
                        "pondId", pond.id().toString(),
                        "scheduledAt", reminder.reminderDate() + "T" + reminder.reminderTime(),
                        "delayMs", String.valueOf(delayMillis)
                ));
    }

    @Override
    public void scheduleHarvestReminder(HarvestReminder reminder, Ponds pond) {
        cancelHarvestReminder(reminder.id());

        long delayMillis = calculateDelay(reminder.reminderDate().atTime(reminder.reminderTime()));

        if (delayMillis <= 0) {
            logManager.warn(LogCategory.SCHEDULER,
                    "Harvest reminder time has passed, skipping: " + reminder.id(),
                    SOURCE);
            return;
        }

        ScheduledFuture<?> task = scheduler.schedule(
                () -> executeHarvestReminder(reminder, pond),
                delayMillis,
                TimeUnit.MILLISECONDS
        );

        harvestReminderTasks.put(reminder.id(), task);

        logManager.info(LogCategory.SCHEDULER,
                "Scheduled harvest reminder: " + reminder.id() + " for pond: " + pond.name(),
                SOURCE,
                Map.of(
                        "reminderId", reminder.id().toString(),
                        "pondId", pond.id().toString(),
                        "scheduledAt", reminder.reminderDate() + "T" + reminder.reminderTime(),
                        "delayMs", String.valueOf(delayMillis)
                ));
    }

    @Override
    public void updateFeedReminder(FeedReminder reminder, Ponds pond) {
        logManager.info(LogCategory.SCHEDULER,
                "Updating feed reminder: " + reminder.id(),
                SOURCE);
        scheduleFeedReminder(reminder, pond);
    }

    @Override
    public void updateHarvestReminder(HarvestReminder reminder, Ponds pond) {
        logManager.info(LogCategory.SCHEDULER,
                "Updating harvest reminder: " + reminder.id(),
                SOURCE);
        scheduleHarvestReminder(reminder, pond);
    }

    @Override
    public void cancelFeedReminder(UUID reminderId) {
        ScheduledFuture<?> task = feedReminderTasks.remove(reminderId);
        if (task != null) {
            task.cancel(false);
            logManager.info(LogCategory.SCHEDULER,
                    "Cancelled feed reminder: " + reminderId,
                    SOURCE);
        }
    }

    @Override
    public void cancelHarvestReminder(UUID reminderId) {
        ScheduledFuture<?> task = harvestReminderTasks.remove(reminderId);
        if (task != null) {
            task.cancel(false);
            logManager.info(LogCategory.SCHEDULER,
                    "Cancelled harvest reminder: " + reminderId,
                    SOURCE);
        }
    }

    private void executeFeedReminder(FeedReminder reminder, Ponds pond) {
        try {
            logManager.info(LogCategory.SCHEDULER,
                    "Executing feed reminder: " + reminder.id(),
                    SOURCE);

            Notification notification = Notification.feedReminder(
                    reminder.organizationId(),
                    reminder.feedType(),
                    pond.name()
            );

            notificationSender.sendToOrganization(reminder.organizationId(), notification);

            feedReminderTasks.remove(reminder.id());

            logManager.info(LogCategory.SCHEDULER,
                    "Feed reminder executed successfully: " + reminder.id(),
                    SOURCE);

        } catch (Exception e) {
            logManager.error(LogCategory.SCHEDULER,
                    "Failed to execute feed reminder: " + reminder.id(),
                    SOURCE,
                    e);
        }
    }

    private void executeHarvestReminder(HarvestReminder reminder, Ponds pond) {
        try {
            logManager.info(LogCategory.SCHEDULER,
                    "Executing harvest reminder: " + reminder.id(),
                    SOURCE);

            Notification notification = Notification.harvestReminder(
                    reminder.organizationId(),
                    pond.name()
            );

            notificationSender.sendToOrganization(reminder.organizationId(), notification);

            harvestReminderTasks.remove(reminder.id());

            logManager.info(LogCategory.SCHEDULER,
                    "Harvest reminder executed successfully: " + reminder.id(),
                    SOURCE);

        } catch (Exception e) {
            logManager.error(LogCategory.SCHEDULER,
                    "Failed to execute harvest reminder: " + reminder.id(),
                    SOURCE,
                    e);
        }
    }

    private long calculateDelay(LocalDateTime targetTime) {
        return Duration.between(LocalDateTime.now(), targetTime).toMillis();
    }

    public void shutdown() {
        logManager.info(LogCategory.SCHEDULER, "Shutting down reminder scheduler", SOURCE);
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
