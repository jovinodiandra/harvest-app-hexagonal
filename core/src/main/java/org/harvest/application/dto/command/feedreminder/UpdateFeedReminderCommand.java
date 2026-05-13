package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateFeedReminderCommand(
        UserSession session,
        UUID id,
        UUID pondId,
        LocalDate reminderDate,
        LocalTime reminderTime,
        String feedType,
        String notes
) {
}
