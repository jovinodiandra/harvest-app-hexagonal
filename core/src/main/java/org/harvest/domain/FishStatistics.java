package org.harvest.domain;

import java.util.UUID;

public record FishStatistics(UUID pondId, int totalPonds,int totalSeedsStocked,int totalFishAlive,int totalFishDead) {
}
