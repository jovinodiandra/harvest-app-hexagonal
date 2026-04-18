package org.harvest.application.dto.result;

import org.harvest.application.dto.value.FeedPriceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record DeleteFeedPriceResult(UUID id, String feedName, BigDecimal pricePerKilogram, LocalDate effectiveDate, FeedPriceStatus status, String description, LocalDateTime deletedAt, UUID deletedBy) {
}
