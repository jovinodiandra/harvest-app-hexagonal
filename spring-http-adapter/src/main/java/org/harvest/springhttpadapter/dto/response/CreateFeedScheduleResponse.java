package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateFeedScheduleResult;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record CreateFeedScheduleResponse(UUID id, UUID pondsId, String pondName, String feedType, BigDecimal feedAmount, LocalTime feedTime) {

    public static CreateFeedScheduleResponse from(CreateFeedScheduleResult result) {
        return new CreateFeedScheduleResponse(result.id(), result.pondsId(), result.pondName(), result.feedType(), result.feedAmount(), result.feedTime());
    }
}
