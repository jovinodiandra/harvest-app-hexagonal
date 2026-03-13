package org.harvest.application.dto.result;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record HarvestReminderItem(UUID id,
                                  UUID pondId,
                                  String pondName,
                                  LocalDate reminderDate,
                                  LocalTime reminderTime,
                                  String notes,
                                  LocalDateTime createdAt) {
}
