package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

public record ViewFeedScheduleQuery (UserSession session, int page, int limit){
}
