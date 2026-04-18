package org.harvest.application.port.outbound;

import org.harvest.domain.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationSender {

    void send(Notification notification);

    void sendToUser(UUID userId, Notification notification);

    void sendToOrganization(UUID organizationId, Notification notification);

    void sendBulk(List<Notification> notifications);
}
