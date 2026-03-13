package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewHarvestReportResult;
import org.harvest.domain.HarvestRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewHarvestReportResponse(List<HarvestRecordItem> data) {

    public static ViewHarvestReportResponse from(ViewHarvestReportResult result) {
        List<HarvestRecordItem> items = result.data().stream()
                .map(HarvestRecordItem::from)
                .toList();
        return new ViewHarvestReportResponse(items);
    }

    public record HarvestRecordItem(UUID id, String pondsName, UUID pondsId, LocalDate harvestDate, UUID organizationId, int harvestFishCount, BigDecimal totalWeight, String notes) {
        public static HarvestRecordItem from(HarvestRecord record) {
            return new HarvestRecordItem(record.id(), record.pondsName(), record.pondsId(), record.harvestDate(), record.organizationId(), record.harvestFishCount(), record.totalWeight(), record.notes());
        }
    }
}
