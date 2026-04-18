package org.harvest.application.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateFeedPrice(String feedName, BigDecimal pricePerKiloGram, LocalDate effectiveDate, String description) {
}
