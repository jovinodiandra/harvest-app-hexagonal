package org.harvest.application.dto.result;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record CreateHarvestReminderResult(UUID id, UUID pondId, LocalDate reminderDate, LocalTime reminderTime, String notes, LocalDateTime createdAt) {
}
