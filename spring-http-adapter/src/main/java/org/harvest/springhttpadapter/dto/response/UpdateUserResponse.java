package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateUserResult;
import org.harvest.application.dto.value.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateUserResponse(UUID id, String name, String email, String role, LocalDateTime updatedAt) {

    public static UpdateUserResponse from(UpdateUserResult result) {
        return new UpdateUserResponse(result.id(), result.name(), result.email(), result.role().getDescription(), result.updatedAt());
    }
}
