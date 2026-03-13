package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewPondsResult;

import java.util.List;
import java.util.UUID;

public record ViewPondsResponse(List<PondsResponse> ponds) {
    record PondsResponse(UUID id, String name, String location, int capacity) {
    }

    public static ViewPondsResponse from(ViewPondsResult pondsResult) {
        return new ViewPondsResponse(pondsResult.ponds().stream().map(pond -> new PondsResponse(pond.id(), pond.name(), pond.location(), pond.capacity())).toList());
    }
}
