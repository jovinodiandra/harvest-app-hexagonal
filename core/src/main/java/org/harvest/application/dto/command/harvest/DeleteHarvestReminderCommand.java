package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record DeleteHarvestReminderCommand(UserSession session, UUID id) {
}
