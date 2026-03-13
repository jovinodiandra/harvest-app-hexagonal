package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateHarvestRecordResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateHarvestRecordResponse(UUID id, UUID pondId, LocalDate harvestDate, int harvestedFishCount, BigDecimal totalWeight, String notes) {

    public static UpdateHarvestRecordResponse from(UpdateHarvestRecordResult result) {
        return new UpdateHarvestRecordResponse(result.id(), result.pondId(), result.harvestDate(), result.harvestedFishCount(), result.totalWeight(), result.notes());
    }
}
