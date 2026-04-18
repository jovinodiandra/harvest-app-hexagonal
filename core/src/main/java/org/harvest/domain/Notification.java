package org.harvest.domain;

import java.util.Map;
import java.util.UUID;

public record Notification(
        UUID id,
        UUID recipientId,
        String title,
        String body,
        NotificationType type,
        Map<String, String> data
) {
    public static Notification create(UUID recipientId, String title, String body, NotificationType type) {
        return new Notification(UUID.randomUUID(), recipientId, title, body, type, Map.of());
    }

    public static Notification feedReminder(UUID recipientId, String feedType, String pondName) {
        return new Notification(
                UUID.randomUUID(),
                recipientId,
                "Pengingat Pakan",
                "Waktunya memberi pakan " + feedType + " di " + pondName,
                NotificationType.FEED_REMINDER,
                Map.of()
        );
    }

    public static Notification harvestReminder(UUID recipientId, String pondName) {
        return new Notification(
                UUID.randomUUID(),
                recipientId,
                "Pengingat Panen",
                "Waktunya panen di " + pondName,
                NotificationType.HARVEST_REMINDER,
                Map.of()
        );
    }

    public Notification withData(Map<String, String> data) {
        return new Notification(id, recipientId, title, body, type, data);
    }
}
