package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.FeedReminderItem;
import org.harvest.application.dto.result.ViewFeedRemindersResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ViewFeedRemindersResponse(List<FeedReminderItemResponse> reminders) {

    public static ViewFeedRemindersResponse from(ViewFeedRemindersResult result) {
        List<FeedReminderItemResponse> items = result.data().stream()
                .map(FeedReminderItemResponse::from)
                .toList();
        return new ViewFeedRemindersResponse(items);
    }

    public record FeedReminderItemResponse(UUID id, UUID pondId, String pondName, LocalDate reminderDate, LocalTime reminderTime, String feedType, String notes, LocalDateTime createdAt) {
        public static FeedReminderItemResponse from(FeedReminderItem item) {
            return new FeedReminderItemResponse(item.id(), item.pondId(), item.pondName(), item.reminderDate(), item.reminderTime(), item.feedType(), item.notes(), item.createdAt());
        }
    }
}
