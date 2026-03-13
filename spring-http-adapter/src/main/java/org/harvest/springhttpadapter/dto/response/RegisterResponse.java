package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.RegisterResult;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterResponse(UUID userId, UUID organizationId, String name, String email, String organizationName,
                               String role, LocalDateTime createdAt) {

    public static RegisterResponse from(RegisterResult result) {
        return new RegisterResponse(
                result.userId(),
                result.organizationId(),
                result.name(),
                result.email(),
                result.organizationName(),
                result.role(),
                result.createdAt()
        );
    }
}
