package org.harvest.springhttpadapter.dto.request;

import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateFeedPriceRequest(String feedName, BigDecimal pricePerKiloGram, LocalDate effectiveDate, String description) {
}
