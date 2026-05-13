package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateFeedPriceCommand(UserSession session, UUID id, String feedName, BigDecimal pricePerKiloGram, LocalDate effectiveDate, String description) {
}
