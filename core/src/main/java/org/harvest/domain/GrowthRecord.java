package org.harvest.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GrowthRecord(UUID id, UUID pondsId, LocalDate recordDate, BigDecimal averageLength, BigDecimal averageWeight, UUID organizationId) {
    public GrowthRecord update(LocalDate recordDate, BigDecimal averageLength, BigDecimal averageWeight) {
        return new GrowthRecord(id, pondsId, recordDate, averageLength, averageWeight, organizationId);
    }
}
