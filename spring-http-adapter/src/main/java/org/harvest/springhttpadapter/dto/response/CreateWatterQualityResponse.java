package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateWatterQualityResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateWatterQualityResponse(UUID id, UUID pondsId, String pondName, LocalDate recordDate, BigDecimal ph, BigDecimal temperature, BigDecimal dissolvedOxygen) {

    public static CreateWatterQualityResponse from(CreateWatterQualityResult result) {
        return new CreateWatterQualityResponse(result.id(), result.pondsId(), result.pondName(), result.recordDate(), result.ph(), result.temperature(), result.dissolvedOxygen());
    }
}
