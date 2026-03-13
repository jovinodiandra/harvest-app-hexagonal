package org.harvest.application.dto.value;

import java.util.UUID;

public record SessionCreationRequest(UUID userId, UUID organizationId, Role role) {
}
