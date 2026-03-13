package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewGrowthRecordResult;
import org.harvest.domain.GrowthRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewGrowthRecordResponse(List<GrowthRecordItem> growthRecords) {

    public static ViewGrowthRecordResponse from(ViewGrowthRecordResult result) {
        List<GrowthRecordItem> items = result.growthRecords().stream()
                .map(GrowthRecordItem::from)
                .toList();
        return new ViewGrowthRecordResponse(items);
    }

    public record GrowthRecordItem(UUID id, UUID pondsId, LocalDate recordDate, BigDecimal averageLength, BigDecimal averageWeight) {
        public static GrowthRecordItem from(GrowthRecord record) {
            return new GrowthRecordItem(record.id(), record.pondsId(), record.recordDate(), record.averageLength(), record.averageWeight());
        }
    }
}
