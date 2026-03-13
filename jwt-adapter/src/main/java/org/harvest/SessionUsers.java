package org.harvest;

import org.harvest.application.dto.value.Role;
import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public class SessionUsers implements UserSession
{
    private final SessionPayload sessionPayload;

    public SessionUsers(SessionPayload sessionPayload) {
        this.sessionPayload = sessionPayload;
    }

    @Override
    public UUID getUserId() {
        return sessionPayload.userId();
    }

    @Override
    public UUID getOrganizationId() {
        return sessionPayload.organizationId();
    }

    @Override
    public Role getRole() {
        return sessionPayload.role();
    }
}
