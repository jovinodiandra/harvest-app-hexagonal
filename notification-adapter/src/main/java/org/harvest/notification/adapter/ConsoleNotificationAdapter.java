package org.harvest.notification.adapter;

import org.harvest.application.port.outbound.LogManager;
import org.harvest.application.port.outbound.NotificationSender;
import org.harvest.application.dto.value.LogCategory;
import org.harvest.domain.Notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Simple console-based notification adapter.
 * Prints notifications to console for development/testing.
 */
public class ConsoleNotificationAdapter implements NotificationSender {

    private static final String SOURCE = "ConsoleNotificationAdapter";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";

    private final LogManager logManager;

    public ConsoleNotificationAdapter(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void send(Notification notification) {
        printNotification(notification, null);
        logManager.info(LogCategory.NOTIFICATION,
                "Notification sent: " + notification.title(),
                SOURCE,
                Map.of(
                        "notificationId", notification.id().toString(),
                        "type", notification.type().name()
                ));
    }

    @Override
    public void sendToUser(UUID userId, Notification notification) {
        printNotification(notification, "User: " + userId);
        logManager.info(LogCategory.NOTIFICATION,
                "Notification sent to user: " + userId,
                SOURCE,
                Map.of(
                        "notificationId", notification.id().toString(),
                        "userId", userId.toString(),
                        "type", notification.type().name()
                ));
    }

    @Override
    public void sendToOrganization(UUID organizationId, Notification notification) {
        printNotification(notification, "Organization: " + organizationId);
        logManager.info(LogCategory.NOTIFICATION,
                "Notification sent to organization: " + organizationId,
                SOURCE,
                Map.of(
                        "notificationId", notification.id().toString(),
                        "organizationId", organizationId.toString(),
                        "type", notification.type().name()
                ));
    }

    @Override
    public void sendBulk(List<Notification> notifications) {
        for (Notification notification : notifications) {
            send(notification);
        }
        logManager.info(LogCategory.NOTIFICATION,
                "Bulk notification sent: " + notifications.size() + " notifications",
                SOURCE);
    }

    private void printNotification(Notification notification, String recipient) {
        String timestamp = FORMATTER.format(LocalDateTime.now());

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(BLUE).append("╔══════════════════════════════════════════════════════════════╗").append(RESET).append("\n");
        sb.append(BLUE).append("║").append(RESET);
        sb.append(YELLOW).append(" 🔔 NOTIFICATION ").append(RESET);
        sb.append(BLUE).append("║").append(RESET).append("\n");
        sb.append(BLUE).append("╠══════════════════════════════════════════════════════════════╣").append(RESET).append("\n");

        sb.append(BLUE).append("║").append(RESET);
        sb.append(CYAN).append(" Time: ").append(RESET).append(timestamp);
        sb.append(BLUE).append("║").append(RESET).append("\n");

        sb.append(BLUE).append("║").append(RESET);
        sb.append(CYAN).append(" Type: ").append(RESET).append(notification.type());
        sb.append(BLUE).append("║").append(RESET).append("\n");

        if (recipient != null) {
            sb.append(BLUE).append("║").append(RESET);
            sb.append(CYAN).append(" To: ").append(RESET).append(recipient);
            sb.append(BLUE).append("║").append(RESET).append("\n");
        }

        sb.append(BLUE).append("╠══════════════════════════════════════════════════════════════╣").append(RESET).append("\n");

        sb.append(BLUE).append("║").append(RESET);
        sb.append(GREEN).append(" ").append(notification.title()).append(RESET);
        sb.append(BLUE).append("║").append(RESET).append("\n");

        sb.append(BLUE).append("║").append(RESET);
        sb.append(" ").append(notification.body());
        sb.append(BLUE).append("║").append(RESET).append("\n");

        if (notification.data() != null && !notification.data().isEmpty()) {
            sb.append(BLUE).append("║").append(RESET);
            sb.append(CYAN).append(" Data: ").append(RESET).append(notification.data());
            sb.append(BLUE).append("║").append(RESET).append("\n");
        }

        sb.append(BLUE).append("╚══════════════════════════════════════════════════════════════╝").append(RESET).append("\n");

        System.out.println(sb);
    }
}
