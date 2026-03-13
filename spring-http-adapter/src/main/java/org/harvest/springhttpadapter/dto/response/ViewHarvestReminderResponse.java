package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.HarvestReminderItem;
import org.harvest.application.dto.result.ViewHarvestReminderResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ViewHarvestReminderResponse(List<HarvestReminderItemResponse> reminders) {

    public static ViewHarvestReminderResponse from(ViewHarvestReminderResult result) {
        List<HarvestReminderItemResponse> items = result.data().stream()
                .map(HarvestReminderItemResponse::from)
                .toList();
        return new ViewHarvestReminderResponse(items);
    }

    public record HarvestReminderItemResponse(UUID id, UUID pondId, String pondName, LocalDate reminderDate, LocalTime reminderTime, String notes, LocalDateTime createdAt) {
        public static HarvestReminderItemResponse from(HarvestReminderItem item) {
            return new HarvestReminderItemResponse(item.id(), item.pondId(), item.pondName(), item.reminderDate(), item.reminderTime(), item.notes(), item.createdAt());
        }
    }
}
