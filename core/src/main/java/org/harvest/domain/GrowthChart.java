package org.harvest.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GrowthChart (UUID pondId, LocalDate date, BigDecimal averageWeight, BigDecimal averageLength){
}
