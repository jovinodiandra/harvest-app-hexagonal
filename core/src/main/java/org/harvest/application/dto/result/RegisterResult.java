package org.harvest.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterResult(UUID userId, UUID organizationId, String name, String email, String organizationName,
                             String role, LocalDateTime createdAt) {
}
