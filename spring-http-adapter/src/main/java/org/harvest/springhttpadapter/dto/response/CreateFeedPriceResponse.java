package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateFeedPriceResult;
import org.harvest.application.dto.value.FeedPriceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateFeedPriceResponse(
        UUID id,

        String feedName,

        BigDecimal pricePerKiloGram,

        LocalDate effectiveDate,

        FeedPriceStatus status,

        LocalDateTime createdAt) {

    public static CreateFeedPriceResponse from(CreateFeedPriceResult result) {
        return new CreateFeedPriceResponse(
                result.id(),
                result.feedName(),
                result.pricePerKiloGram(),
                result.effectiveDate(),
                result.status(),
                result.createdAt());
    }
}
