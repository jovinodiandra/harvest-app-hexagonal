package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateHarvestRecordCommand(
        UserSession session,
        UUID id,
        UUID pondId,
        LocalDate harvestDate,
        int harvestedFishCount,
        BigDecimal totalWeight,
        String notes
) {
}
