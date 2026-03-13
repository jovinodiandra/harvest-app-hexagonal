package org.harvest.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdatePondsResult (UUID id, String name, String location, int capacity, LocalDateTime updatedAt){
}
