package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateDiseasesRecordResult;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateDiseasesRecordResponse(
        UUID id,
        UUID pondId,
        String diseaseName,
        String symptoms,
        int infectedFishCount,
        LocalDate diseaseDate,
        String notes
) {
    public static UpdateDiseasesRecordResponse from(UpdateDiseasesRecordResult result) {
        return new UpdateDiseasesRecordResponse(
                result.id(),
                result.pondId(),
                result.diseaseName(),
                result.symptoms(),
                result.infectedFishCount(),
                result.diseaseDate(),
                result.notes()
        );
    }
}
