package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewSeedResult;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewSeedResponse(List<SeedResponse> seeds) {
    record  SeedResponse(UUID id, UUID pondId, String type, int quantity, LocalDate stockDate){}
    public static ViewSeedResponse from(ViewSeedResult seedsResult) {
        return new ViewSeedResponse(seedsResult.seeds().stream().map(seed ->  new SeedResponse(seed.id(),seed.pondsId(),seed.type(),seed.quantity(),seed.stockDate())).toList());
    }
}
