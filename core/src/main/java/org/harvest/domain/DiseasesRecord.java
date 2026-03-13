package org.harvest.domain;

import java.time.LocalDate;
import java.util.UUID;

public record DiseasesRecord(UUID id, UUID pondsId, String diseaseName, String symptoms, int infectedFishCount, LocalDate diseasesDate, String notes, UUID organizationId) {
    public DiseasesRecord update(UUID pondsId, String diseaseName, String symptoms, int infectedFishCount, LocalDate diseasesDate, String notes){
        return new DiseasesRecord(id, pondsId, diseaseName, symptoms, infectedFishCount, diseasesDate, notes, organizationId);
    }
}
