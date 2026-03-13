package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateFeedReminderResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateFeedReminderResponse(UUID id, UUID pondId, LocalDate reminderDate, LocalTime reminderTime, String feedType, String notes, LocalDateTime updatedAt) {

    public static UpdateFeedReminderResponse from(UpdateFeedReminderResult result) {
        return new UpdateFeedReminderResponse(result.id(), result.pondId(), result.reminderDate(), result.reminderTime(), result.feedType(), result.notes(), result.updatedAt());
    }
}
