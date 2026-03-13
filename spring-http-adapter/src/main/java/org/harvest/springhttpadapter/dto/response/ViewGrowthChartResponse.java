package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewGrowthChartResult;
import org.harvest.domain.GrowthChart;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewGrowthChartResponse(List<GrowthChartItem> growthCharts) {

    public static ViewGrowthChartResponse from(ViewGrowthChartResult result) {
        List<GrowthChartItem> items = result.growthCharts().stream()
                .map(GrowthChartItem::from)
                .toList();
        return new ViewGrowthChartResponse(items);
    }

    public record GrowthChartItem(UUID pondId, LocalDate date, BigDecimal averageWeight, BigDecimal averageLength) {
        public static GrowthChartItem from(GrowthChart chart) {
            return new GrowthChartItem(
                    chart.pondId(),
                    chart.date(),
                    chart.averageWeight(),
                    chart.averageLength()
            );
        }
    }
}
