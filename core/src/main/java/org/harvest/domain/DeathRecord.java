package org.harvest.domain;

import java.time.LocalDate;
import java.util.UUID;

public record DeathRecord (UUID id, UUID pondsId, LocalDate recordDate, int deathCount, String notes, UUID organizationId){
    public DeathRecord update(LocalDate recordDate, int deathCount, String notes) {
        return new DeathRecord(id, pondsId, recordDate, deathCount, notes, organizationId);
    }
}
