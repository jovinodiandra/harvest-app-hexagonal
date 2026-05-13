package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record UpdatePondsCommand(UserSession session, String name, String location, int capacity, UUID pondsId) {
}
