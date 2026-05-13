package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

public record CreateUserCommand(UserSession userSession, String name, String email, String password) {
}
