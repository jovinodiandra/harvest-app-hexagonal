package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateHarvestRecordCommand(UserSession session, UUID pondId, LocalDate harvestDate, int harvestFishCount, BigDecimal totalWeight, String notes) {
}
