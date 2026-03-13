package org.harvest.application.port.outbound;

import org.harvest.domain.GrowthChart;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GrowthChartRepository {
     List<GrowthChart> reportGrowthChart(UUID organizationId, UUID pondsId, LocalDate startDate, LocalDate endDate);
}
