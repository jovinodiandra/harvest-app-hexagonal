package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

public record CreatePondsCommand(UserSession session, String name, String location, int capacity) {
}
