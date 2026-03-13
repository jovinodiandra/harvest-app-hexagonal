package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewHarvestRecordResult;
import org.harvest.domain.HarvestRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewHarvestRecordResponse(List<HarvestRecordItem> harvestRecords) {

    public static ViewHarvestRecordResponse from(ViewHarvestRecordResult result) {
        List<HarvestRecordItem> items = result.harvestRecords().stream()
                .map(HarvestRecordItem::from)
                .toList();
        return new ViewHarvestRecordResponse(items);
    }

    public record HarvestRecordItem(UUID id, String pondsName, UUID pondsId, LocalDate harvestDate, UUID organizationId, int harvestFishCount, BigDecimal totalWeight, String notes) {
        public static HarvestRecordItem from(HarvestRecord record) {
            return new HarvestRecordItem(record.id(), record.pondsName(), record.pondsId(), record.harvestDate(), record.organizationId(), record.harvestFishCount(), record.totalWeight(), record.notes());
        }
    }
}
