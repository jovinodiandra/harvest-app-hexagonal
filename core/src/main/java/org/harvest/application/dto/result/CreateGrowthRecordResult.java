package org.harvest.application.dto.result;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateGrowthRecordResult (UUID id, UUID pondsId, String pondName, LocalDate recordDate, BigDecimal averageLength, BigDecimal averageWeight){
}
