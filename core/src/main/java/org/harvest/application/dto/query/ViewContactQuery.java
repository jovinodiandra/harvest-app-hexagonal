package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Contact;

import java.util.List;
import java.util.UUID;

public record ViewContactQuery(UserSession session, UUID supplierId, String name, int page, int limit) {
}
