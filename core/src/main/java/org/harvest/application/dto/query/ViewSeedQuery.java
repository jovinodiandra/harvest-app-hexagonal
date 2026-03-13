package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record ViewSeedQuery(UserSession session, int page, int limit) {
}
