package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record ViewDeathRecordQuery (UserSession session, UUID pondsId, int page, int limit){
}
