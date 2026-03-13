package org.harvest.application.dto.result;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

public record FeedReminderItem(
        UUID id,
        UUID pondId,
        String pondName,
        LocalDate reminderDate,
        LocalTime reminderTime,
        String feedType,
        String notes,
        LocalDateTime createdAt
) {
}
