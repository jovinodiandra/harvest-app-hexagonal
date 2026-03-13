package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record VIewDiseasesRecordQuery (UserSession session, UUID pondId, int page, int limit){
}
