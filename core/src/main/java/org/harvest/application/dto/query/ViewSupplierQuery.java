package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

public record ViewSupplierQuery(UserSession session, String name, int page, int limit) {
}
