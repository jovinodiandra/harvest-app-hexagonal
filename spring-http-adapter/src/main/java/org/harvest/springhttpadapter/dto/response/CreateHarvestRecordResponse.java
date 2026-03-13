package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateHarvestRecordResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateHarvestRecordResponse(UUID id, UUID pondId, LocalDate harvestDate, int harvestFishCount, BigDecimal totalWeight, String notes) {

    public static CreateHarvestRecordResponse from(CreateHarvestRecordResult result) {
        return new CreateHarvestRecordResponse(result.id(), result.pondId(), result.harvestDate(), result.harvestFishCount(), result.totalWeight(), result.notes());
    }
}
