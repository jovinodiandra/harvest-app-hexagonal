package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateHarvestReminderResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateHarvestReminderResponse(UUID id, UUID pondId, LocalDate reminderDate, LocalTime reminderTime, String notes, LocalDateTime updatedAt) {

    public static UpdateHarvestReminderResponse from(UpdateHarvestReminderResult result) {
        return new UpdateHarvestReminderResponse(result.id(), result.pondId(), result.reminderDate(), result.reminderTime(), result.notes(), result.updatedAt());
    }
}
