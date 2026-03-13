package org.harvest.application.dto.result;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDiseasesRecordResult(
        UUID id,
        UUID pondId,
        String diseaseName,
        String symptoms,
        int infectedFishCount,
        LocalDate diseaseDate,
        String notes
) {
}
