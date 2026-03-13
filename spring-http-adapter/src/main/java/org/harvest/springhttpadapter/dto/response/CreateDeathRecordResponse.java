package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateDeathRecordResult;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDeathRecordResponse(UUID id, UUID pondsId, String pondName, LocalDate recordDate, int deathCount, String notes) {

    public static CreateDeathRecordResponse from(CreateDeathRecordResult result) {
        return new CreateDeathRecordResponse(result.id(), result.pondsId(), result.pondName(), result.recordDate(), result.deathCount(), result.notes());
    }
}
