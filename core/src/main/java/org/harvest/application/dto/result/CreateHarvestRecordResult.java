package org.harvest.application.dto.result;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateHarvestRecordResult (UUID id, UUID pondId, LocalDate harvestDate, int harvestFishCount, BigDecimal totalWeight, String notes, LocalDateTime createdAt){
}
