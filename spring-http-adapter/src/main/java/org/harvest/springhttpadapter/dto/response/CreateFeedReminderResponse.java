package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateFeedReminderResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record CreateFeedReminderResponse(UUID id, UUID pondId, LocalDate reminderDate, LocalTime reminderTime, String feedType, String notes, LocalDateTime createdAt) {

    public static CreateFeedReminderResponse from(CreateFeedReminderResult result) {
        return new CreateFeedReminderResponse(result.id(), result.pondId(), result.reminderDate(), result.reminderTime(), result.feedType(), result.notes(), result.createdAt());
    }
}
