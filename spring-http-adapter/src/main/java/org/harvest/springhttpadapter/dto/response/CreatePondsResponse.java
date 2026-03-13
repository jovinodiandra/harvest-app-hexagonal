package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreatePondsResult;
import org.harvest.springhttpadapter.dto.request.CreatePondsRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePondsResponse(UUID id, String name, String location, int capacity, LocalDateTime createdAt) {

    public static CreatePondsResponse from(CreatePondsResult result){
        return new CreatePondsResponse(result.id(), result.name(), result.location(), result.capacity(), result.createdAt());
    }
}
