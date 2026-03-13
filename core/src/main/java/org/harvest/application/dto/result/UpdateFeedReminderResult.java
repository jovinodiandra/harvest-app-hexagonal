package org.harvest.application.dto.result;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateFeedReminderResult(
        UUID id,
        UUID pondId,
        LocalDate reminderDate,
        LocalTime reminderTime,
        String feedType,
        String notes,
        LocalDateTime updatedAt
) {
}
