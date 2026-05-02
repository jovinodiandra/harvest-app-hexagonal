package org.harvest.application.dto.result;

import org.harvest.application.dto.value.FeedPriceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateFeedPriceResult(UUID id, String feedName, BigDecimal pricePerKiloGram, LocalDate effectiveDate, FeedPriceStatus status,String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
