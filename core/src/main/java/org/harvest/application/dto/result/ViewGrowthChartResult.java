package org.harvest.application.dto.result;

import org.harvest.domain.GrowthChart;

import java.util.List;

public record ViewGrowthChartResult(List<GrowthChart> growthCharts) {
}
