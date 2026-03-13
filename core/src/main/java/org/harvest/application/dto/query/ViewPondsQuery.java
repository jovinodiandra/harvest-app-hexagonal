package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

public record ViewPondsQuery(UserSession session, int page, int limit) {
}
