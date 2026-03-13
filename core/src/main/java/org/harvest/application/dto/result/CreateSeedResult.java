package org.harvest.application.dto.result;

import org.harvest.domain.Ponds;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSeedResult (UUID id, UUID pondsId, String name, int quantity, LocalDate stockDate, LocalDateTime createdAt){
}
