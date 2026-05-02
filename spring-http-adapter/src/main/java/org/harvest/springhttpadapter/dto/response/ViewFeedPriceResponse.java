package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewFeedPriceResult;
import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.domain.FeedPrice;
import org.harvest.domain.value.FeedName;
import org.harvest.domain.value.Price;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewFeedPriceResponse(List<FeedPriceItem> feedPrice) {

    public static ViewFeedPriceResponse from(ViewFeedPriceResult result) {
        List<FeedPriceItem> list = result.feedPrices().stream().map(FeedPriceItem::from).toList();
        return new ViewFeedPriceResponse(list);
    }

    public record FeedPriceItem(
            UUID id,
            String feedName,
            BigDecimal pricePerKiloGram,
            LocalDate effectiveDate,
            FeedPriceStatus status,
            String description) {
        public static FeedPriceItem from(FeedPrice feedPrice) {
            return new FeedPriceItem(
                    feedPrice.getId(),
                    feedPrice.getFeedName().getValue(),
                    feedPrice.getPricePerKiloGram().getValue(),
                    feedPrice.getEffectiveDate(),
                    feedPrice.getStatus(),
                    feedPrice.getDescription());
        }
    }
}
