package org.harvest;

import org.harvest.application.dto.value.Role;

import java.util.UUID;

public record SessionPayload (UUID userId, UUID organizationId, Role role){
}
