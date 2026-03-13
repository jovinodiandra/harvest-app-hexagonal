package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewWatterQualityResult;
import org.harvest.domain.WatterQuality;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewWatterQualityResponse(List<WatterQualityItem> watterQualities) {

    public static ViewWatterQualityResponse from(ViewWatterQualityResult result) {
        List<WatterQualityItem> items = result.watterQualities().stream()
                .map(WatterQualityItem::from)
                .toList();
        return new ViewWatterQualityResponse(items);
    }

    public record WatterQualityItem(UUID id, UUID pondsId, LocalDate recordDate, BigDecimal ph, BigDecimal temperature, BigDecimal dissolvedOxygen) {
        public static WatterQualityItem from(WatterQuality record) {
            return new WatterQualityItem(record.id(), record.pondsID(), record.recordDate(), record.ph(), record.temperature(), record.dissolvedOxygen());
        }
    }
}
