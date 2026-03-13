package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateUserResult;
import org.harvest.application.dto.value.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserResponse(UUID id, String name, String email, Role role, LocalDateTime createdAt) {

    public static CreateUserResponse from(CreateUserResult result) {
        return new CreateUserResponse(result.id(), result.name(), result.email(), result.role(), result.createdAt());
    }
}
