package org.harvest.application.port.outbound.security;

import org.harvest.application.dto.value.Role;

import java.util.UUID;

public interface UserSession {
    UUID getUserId();

    UUID getOrganizationId();

    Role getRole();
}
