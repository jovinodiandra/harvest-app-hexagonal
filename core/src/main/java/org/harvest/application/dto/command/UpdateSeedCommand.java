package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateSeedCommand (UserSession session, UUID pondsId,UUID seedId, String type, int quantity, LocalDate stockDate){
}
