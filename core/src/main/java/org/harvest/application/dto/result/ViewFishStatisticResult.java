package org.harvest.application.dto.result;

import org.harvest.domain.FishStatistics;

import java.util.List;

public record ViewFishStatisticResult(List<FishStatistics> fishStatistics) {
}
