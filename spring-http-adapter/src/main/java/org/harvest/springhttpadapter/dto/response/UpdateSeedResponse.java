package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateSeedResult;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateSeedResponse(UUID id, UUID pondsId,String pondName, String type, int quantity, LocalDate stockDate) {

    public static UpdateSeedResponse from(UpdateSeedResult result) {
        return new UpdateSeedResponse( result.id(), result.pondsId(), result.pondName(), result.type(), result.quantity(), result.stockDate());
    }
}
