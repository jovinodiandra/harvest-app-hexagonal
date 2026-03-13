package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewFeedScheduleResult;
import org.harvest.domain.FeedSchedule;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ViewFeedScheduleResponse(List<FeedScheduleItem> feedSchedules) {

    public static ViewFeedScheduleResponse from(ViewFeedScheduleResult result) {
        List<FeedScheduleItem> items = result.feedSchedules().stream()
                .map(FeedScheduleItem::from)
                .toList();
        return new ViewFeedScheduleResponse(items);
    }

    public record FeedScheduleItem(UUID id, UUID pondsId, String feedType, BigDecimal feedAmount, LocalTime feedTime) {
        public static FeedScheduleItem from(FeedSchedule schedule) {
            return new FeedScheduleItem(schedule.id(), schedule.pondsId(), schedule.feedType(), schedule.feedAmount(), schedule.feedTime());
        }
    }
}
