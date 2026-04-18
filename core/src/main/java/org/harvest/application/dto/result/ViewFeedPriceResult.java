package org.harvest.application.dto.result;

import org.harvest.domain.FeedPrice;

import java.util.List;

public record ViewFeedPriceResult(List<FeedPrice> feedPrices) {
}
