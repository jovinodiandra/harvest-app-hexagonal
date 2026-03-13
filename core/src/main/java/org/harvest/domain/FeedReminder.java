package org.harvest.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record FeedReminder(UUID id, UUID pondsId, LocalDate reminderDate, LocalTime reminderTime, String feedType, String notes, UUID organizationId) {
    public FeedReminder update(UUID pondsId, LocalDate reminderDate, LocalTime reminderTime, String feedType, String notes) {
        return new FeedReminder(id, pondsId, reminderDate, reminderTime, feedType, notes, organizationId);
    }
}

