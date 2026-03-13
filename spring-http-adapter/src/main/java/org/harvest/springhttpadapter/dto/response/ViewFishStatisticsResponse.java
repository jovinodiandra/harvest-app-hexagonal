package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewFishStatisticResult;
import org.harvest.domain.FishStatistics;

import java.util.List;
import java.util.UUID;

public record ViewFishStatisticsResponse(List<FishStatisticsItem> fishStatistics) {

    public static ViewFishStatisticsResponse from(ViewFishStatisticResult result) {
        List<FishStatisticsItem> items = result.fishStatistics().stream()
                .map(FishStatisticsItem::from)
                .toList();
        return new ViewFishStatisticsResponse(items);
    }

    public record FishStatisticsItem(UUID pondId, int totalPonds, int totalSeedsStocked, int totalFishAlive, int totalFishDead) {
        public static FishStatisticsItem from(FishStatistics statistics) {
            return new FishStatisticsItem(
                    statistics.pondId(),
                    statistics.totalPonds(),
                    statistics.totalSeedsStocked(),
                    statistics.totalFishAlive(),
                    statistics.totalFishDead()
            );
        }
    }
}
