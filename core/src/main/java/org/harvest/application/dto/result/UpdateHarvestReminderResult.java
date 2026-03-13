package org.harvest.application.dto.result;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateHarvestReminderResult(
                                          UUID id,
                                          UUID pondId,
                                          LocalDate reminderDate,
                                          LocalTime reminderTime,
                                          String notes,
                                          LocalDateTime updatedAt) {
}
