package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateFeedScheduleResult;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateFeedScheduleResponse(UUID id, UUID pondsId, String pondName, String feedType, BigDecimal feedAmount, LocalTime feedTime) {

    public static UpdateFeedScheduleResponse from(UpdateFeedScheduleResult result) {
        return new UpdateFeedScheduleResponse(result.id(), result.pondsId(), result.pondName(), result.feedType(), result.feedAmount(), result.feedTime());
    }
}
