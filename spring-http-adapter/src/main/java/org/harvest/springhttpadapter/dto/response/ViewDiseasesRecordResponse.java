package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewDiseasesRecordResult;
import org.harvest.domain.DiseasesRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewDiseasesRecordResponse(List<DiseasesRecordItem> records) {

    public static ViewDiseasesRecordResponse from(ViewDiseasesRecordResult result) {
        List<DiseasesRecordItem> items = result.diseasesRecords().stream()
                .map(DiseasesRecordItem::from)
                .toList();
        return new ViewDiseasesRecordResponse(items);
    }

    public record DiseasesRecordItem(
            UUID id,
            UUID pondId,
            String diseaseName,
            String symptoms,
            int infectedFishCount,
            LocalDate diseaseDate,
            String notes
    ) {
        public static DiseasesRecordItem from(DiseasesRecord record) {
            return new DiseasesRecordItem(
                    record.id(),
                    record.pondsId(),
                    record.diseaseName(),
                    record.symptoms(),
                    record.infectedFishCount(),
                    record.diseasesDate(),
                    record.notes()
            );
        }
    }
}
