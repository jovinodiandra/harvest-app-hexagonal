package org.harvest.application.dto.result;

import org.harvest.application.dto.value.Role;

import java.util.UUID;

public record LoginResult(UUID useerId, String name, String email, Role role, UUID organizationId,
                          String organizationName, String sessionId) {
}
