package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewDeathRecordResult;
import org.harvest.domain.DeathRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewDeathRecordResponse(List<DeathRecordItem> deathRecords) {

    public static ViewDeathRecordResponse from(ViewDeathRecordResult result) {
        List<DeathRecordItem> items = result.deathRecords().stream()
                .map(DeathRecordItem::from)
                .toList();
        return new ViewDeathRecordResponse(items);
    }

    public record DeathRecordItem(UUID id, UUID pondsId, LocalDate recordDate, int deathCount, String notes, UUID organizationId) {
        public static DeathRecordItem from(DeathRecord record) {
            return new DeathRecordItem(record.id(), record.pondsId(), record.recordDate(), record.deathCount(), record.notes(), record.organizationId());
        }
    }
}
