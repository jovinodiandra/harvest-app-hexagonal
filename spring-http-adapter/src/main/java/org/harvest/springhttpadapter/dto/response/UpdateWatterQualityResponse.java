package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateWatterQualityResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateWatterQualityResponse(UUID id, UUID pondsId, String pondName, LocalDate recordDate, BigDecimal ph, BigDecimal temperature, BigDecimal dissolvedOxygen) {

    public static UpdateWatterQualityResponse from(UpdateWatterQualityResult result) {
        return new UpdateWatterQualityResponse(result.id(), result.pondsId(), result.pondName(), result.recordDate(), result.ph(), result.temperature(), result.dissolvedOxygen());
    }
}
