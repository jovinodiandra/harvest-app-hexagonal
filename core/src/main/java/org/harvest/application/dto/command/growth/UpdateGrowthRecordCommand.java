package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateGrowthRecordCommand(UserSession session, UUID pondsId, UUID growthRecordId, LocalDate recordDate, BigDecimal averageLength, BigDecimal averageWeight) {
}
