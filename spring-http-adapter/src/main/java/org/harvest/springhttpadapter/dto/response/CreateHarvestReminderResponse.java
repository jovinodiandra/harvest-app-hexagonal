package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateHarvestReminderResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record CreateHarvestReminderResponse(UUID id, UUID pondId, LocalDate reminderDate, LocalTime reminderTime, String notes, LocalDateTime createdAt) {

    public static CreateHarvestReminderResponse from(CreateHarvestReminderResult result) {
        return new CreateHarvestReminderResponse(result.id(), result.pondId(), result.reminderDate(), result.reminderTime(), result.notes(), result.createdAt());
    }
}
