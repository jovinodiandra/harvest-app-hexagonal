package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateDiseasesRecordResult;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDiseasesRecordResponse(
        UUID id,
        UUID pondId,
        String diseaseName,
        String symptoms,
        int infectedFishCount,
        LocalDate diseaseDate,
        String notes
) {
    public static CreateDiseasesRecordResponse from(CreateDiseasesRecordResult result) {
        return new CreateDiseasesRecordResponse(
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
