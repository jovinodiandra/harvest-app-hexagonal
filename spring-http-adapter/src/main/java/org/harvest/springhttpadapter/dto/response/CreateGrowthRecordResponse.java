package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateGrowthRecordResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateGrowthRecordResponse(UUID id, UUID pondsId, String pondName, LocalDate recordDate, BigDecimal averageLength, BigDecimal averageWeight) {

    public static CreateGrowthRecordResponse from(CreateGrowthRecordResult result) {
        return new CreateGrowthRecordResponse(result.id(), result.pondsId(), result.pondName(), result.recordDate(), result.averageLength(), result.averageWeight());
    }
}
