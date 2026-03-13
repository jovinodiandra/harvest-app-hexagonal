package org.harvest.application.port.outbound;

import org.harvest.domain.FishStatistics;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FishStatisticsRepository {
    List<FishStatistics> reportFishStatistics(UUID organizationId, LocalDate startDate, LocalDate endDate);
}
