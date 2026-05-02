package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.DeleteFeedPriceResult;
import org.harvest.application.dto.value.FeedPriceStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeleteFeedPriceResponse(UUID id, String feedName, FeedPriceStatus status, String reason, UUID deletedBy,
                                      LocalDateTime deletedAt) {

    public static DeleteFeedPriceResponse from(DeleteFeedPriceResult result) {
        return new DeleteFeedPriceResponse(result.id(), result.feedName(), result.status(), result.description(), result.deletedBy(), result.deletedAt());
    }



}
