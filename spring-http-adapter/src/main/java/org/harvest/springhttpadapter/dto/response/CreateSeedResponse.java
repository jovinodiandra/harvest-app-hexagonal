package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateSeedResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSeedResponse(UUID id, UUID pondsId, String type, int quantity, LocalDate stockDate, LocalDateTime createdAt) {

    public static CreateSeedResponse from(CreateSeedResult result){
        return new CreateSeedResponse(result.id(),result.pondsId(),result.name(),result.quantity(),result.stockDate(),result.createdAt());
    }
}
