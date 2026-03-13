package org.harvest.application.dto.command;

import org.harvest.application.dto.value.Role;
import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record UpdateUserCommand(UserSession session,UUID userId,  String name, Role role ) {
}
