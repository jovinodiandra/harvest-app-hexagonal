package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateDeathRecordResult;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateDeathRecordResponse(UUID id, UUID pondsId, String pondName, LocalDate recordDate, int deathCount, String notes) {

    public static UpdateDeathRecordResponse from(UpdateDeathRecordResult result) {
        return new UpdateDeathRecordResponse(result.id(), result.pondsId(), result.pondName(), result.recordDate(), result.deathCount(), result.notes());
    }
}
