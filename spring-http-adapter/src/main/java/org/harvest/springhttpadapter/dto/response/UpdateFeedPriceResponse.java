package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateFeedPriceResult;
import org.harvest.application.dto.value.FeedPriceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateFeedPriceResponse(UUID id, String name, BigDecimal pricePerKiloGram, LocalDate effectiveDate, FeedPriceStatus status, String reason, LocalDateTime updatedAt) {

    public static UpdateFeedPriceResponse from(UpdateFeedPriceResult result){
        return new UpdateFeedPriceResponse(result.id(),result.feedName(),result.pricePerKiloGram(), result.effectiveDate(),result.status(),result.description(),result.updatedAt());
    }
}