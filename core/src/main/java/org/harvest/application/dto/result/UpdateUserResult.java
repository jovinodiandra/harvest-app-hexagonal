package org.harvest.application.dto.result;

import org.harvest.application.dto.value.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateUserResult(UUID id, String name, String email, Role role, LocalDateTime updatedAt) {
}
