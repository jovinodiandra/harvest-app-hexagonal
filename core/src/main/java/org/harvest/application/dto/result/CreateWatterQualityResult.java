package org.harvest.application.dto.result;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateWatterQualityResult (UUID id, UUID pondsId, String pondName, LocalDate recordDate, BigDecimal ph, BigDecimal temperature, BigDecimal dissolvedOxygen){
}
