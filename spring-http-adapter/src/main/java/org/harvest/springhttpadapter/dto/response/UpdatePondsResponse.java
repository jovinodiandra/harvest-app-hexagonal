package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdatePondsResult;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdatePondsResponse (UUID id, String name, String location, int capacity, LocalDateTime updatedAt){

    public static UpdatePondsResponse from(UpdatePondsResult result){
        return new UpdatePondsResponse(result.id(), result.name(), result.location(), result.capacity(), result.updatedAt());
    }
}
