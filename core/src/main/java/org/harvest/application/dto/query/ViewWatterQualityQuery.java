package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record ViewWatterQualityQuery (UserSession session, int page, int limit, UUID pondsId){
}
