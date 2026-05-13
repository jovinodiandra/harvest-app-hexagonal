package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record CreateSeedCommand(UserSession session, UUID pondsId, String type, int quantity, LocalDate stockDate) {
}
