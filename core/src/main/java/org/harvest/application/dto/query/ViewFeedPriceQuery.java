package org.harvest.application.dto.query;

import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.application.port.outbound.security.UserSession;

public record ViewFeedPriceQuery(UserSession session, FeedPriceStatus status,  int page, int limit) {
}
