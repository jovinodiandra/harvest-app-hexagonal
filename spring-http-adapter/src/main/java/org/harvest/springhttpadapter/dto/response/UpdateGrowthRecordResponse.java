package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateGrowthRecordResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateGrowthRecordResponse(UUID id, UUID pondsId, String pondName, LocalDate recordDate, BigDecimal averageLength, BigDecimal averageWeight) {

    public static UpdateGrowthRecordResponse from(UpdateGrowthRecordResult result) {
        return new UpdateGrowthRecordResponse(result.id(), result.pondsId(), result.pondName(), result.recordDate(), result.averageLength(), result.averageWeight());
    }
}
