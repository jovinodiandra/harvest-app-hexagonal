package org.harvest.domain;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.UUID;

public record FeedSchedule(UUID id, UUID pondsId, String feedType, BigDecimal feedAmount, LocalTime feedTime, UUID organizationId) {
    public FeedSchedule update(String feedType, BigDecimal feedAmount, LocalTime feedTime) {
        return new FeedSchedule(id, pondsId, feedType, feedAmount, feedTime, organizationId);
    }
}
