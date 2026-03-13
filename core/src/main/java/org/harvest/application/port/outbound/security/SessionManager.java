package org.harvest.application.port.outbound.security;

import org.harvest.application.dto.value.SessionCreationRequest;

import java.time.Instant;
import java.util.UUID;

public interface SessionManager {
    String createSession(SessionCreationRequest request);

    void invalidateAllSessions(UUID userId);
    UserSession getSession(String token);
    Instant now();
}
