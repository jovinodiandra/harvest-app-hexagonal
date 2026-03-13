package org.harvest.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record WatterQuality(UUID id, UUID pondsID, LocalDate recordDate, BigDecimal ph, BigDecimal temperature, BigDecimal dissolvedOxygen, UUID organizationId) {
    public WatterQuality update(BigDecimal ph, BigDecimal temperature, BigDecimal dissolvedOxygen) {
        return new WatterQuality(id, pondsID, LocalDate.now(), ph, temperature, dissolvedOxygen, organizationId);
    }

}
