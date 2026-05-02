package org.harvest.notification.adapter;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.harvest.application.port.outbound.LogManager;
import org.harvest.application.port.outbound.NotificationSender;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.application.dto.value.LogCategory;
import org.harvest.domain.Notification;
import org.harvest.domain.User;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * SMTP-based notification adapter.
 * Sends notifications via email using SMTP protocol.
 */
public class SmtpNotificationAdapter implements NotificationSender {

    private static final String SOURCE = "SmtpNotificationAdapter";

    private final SmtpConfig config;
    private final UserRepository userRepository;
    private final LogManager logManager;
    private final Session mailSession;

    public SmtpNotificationAdapter(
            SmtpConfig config,
            UserRepository userRepository,
            LogManager logManager
    ) {
        this.config = config;
        this.userRepository = userRepository;
        this.logManager = logManager;
        this.mailSession = createMailSession();
    }

    @Override
    public void send(Notification notification) {
        if (notification.recipientId() == null) {
            logManager.warn(LogCategory.NOTIFICATION,
                    "Cannot send notification without recipient ID",
                    SOURCE);
            return;
        }
        sendToUser(notification.recipientId(), notification);
    }

    @Override
    public void sendToUser(UUID userId, Notification notification) {
        User user = userRepository.findById(userId);
        if (user == null) {
            logManager.warn(LogCategory.NOTIFICATION,
                    "User not found: " + userId,
                    SOURCE);
            return;
        }

        sendEmail(user.email(), notification);
    }

    @Override
    public void sendToOrganization(UUID organizationId, Notification notification) {
        int page = 0;
        int limit = 100;
        List<User> users = userRepository.findAllByOrganizationId(organizationId, new Pagination(page, limit));

        if (users.isEmpty()) {
            logManager.warn(LogCategory.NOTIFICATION,
                    "No users found for organization: " + organizationId,
                    SOURCE);
            return;
        }

        logManager.info(LogCategory.NOTIFICATION,
                "Sending notification to " + users.size() + " users in organization: " + organizationId,
                SOURCE);

        for (User user : users) {
            sendEmail(user.email(), notification);
        }
    }

    @Override
    public void sendBulk(List<Notification> notifications) {
        logManager.info(LogCategory.NOTIFICATION,
                "Sending bulk notification: " + notifications.size() + " notifications",
                SOURCE);

        for (Notification notification : notifications) {
            send(notification);
        }
    }

    private void sendEmail(String toEmail, Notification notification) {
        try {
            MimeMessage message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(config.fromEmail(), config.fromName()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(notification.title());
            message.setContent(buildHtmlContent(notification), "text/html; charset=utf-8");

            Transport.send(message);

            logManager.info(LogCategory.NOTIFICATION,
                    "Email sent successfully to: " + toEmail,
                    SOURCE,
                    Map.of(
                            "notificationId", notification.id().toString(),
                            "type", notification.type().name(),
                            "recipient", toEmail
                    ));

        } catch (MessagingException e) {
            logManager.error(LogCategory.NOTIFICATION,
                    "Failed to send email to: " + toEmail,
                    SOURCE,
                    e,
                    Map.of(
                            "notificationId", notification.id().toString(),
                            "recipient", toEmail
                    ));
        } catch (Exception e) {
            logManager.error(LogCategory.NOTIFICATION,
                    "Unexpected error sending email to: " + toEmail,
                    SOURCE,
                    e);
        }
    }

    private Session createMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", config.host());
        props.put("mail.smtp.port", String.valueOf(config.port()));
        props.put("mail.smtp.auth", String.valueOf(config.useAuth()));
        props.put("mail.smtp.starttls.enable", String.valueOf(config.useTls()));

        if (config.useAuth()) {
            return Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.username(), config.password());
                }
            });
        }

        return Session.getInstance(props);
    }

    private String buildHtmlContent(Notification notification) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'></head>");
        html.append("<body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>");

        html.append("<div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 20px; border-radius: 10px 10px 0 0;'>");
        html.append("<h1 style='color: white; margin: 0;'>").append(escapeHtml(notification.title())).append("</h1>");
        html.append("</div>");

        html.append("<div style='background: #f8f9fa; padding: 20px; border: 1px solid #e9ecef; border-top: none;'>");
        html.append("<p style='font-size: 16px; color: #333; line-height: 1.6;'>");
        html.append(escapeHtml(notification.body()));
        html.append("</p>");

        if (notification.data() != null && !notification.data().isEmpty()) {
            html.append("<hr style='border: none; border-top: 1px solid #e9ecef; margin: 20px 0;'>");
            html.append("<div style='background: white; padding: 15px; border-radius: 5px;'>");
            html.append("<h3 style='color: #666; margin-top: 0;'>Detail:</h3>");
            html.append("<ul style='margin: 0; padding-left: 20px;'>");
            for (Map.Entry<String, String> entry : notification.data().entrySet()) {
                html.append("<li><strong>").append(escapeHtml(entry.getKey())).append(":</strong> ");
                html.append(escapeHtml(entry.getValue())).append("</li>");
            }
            html.append("</ul></div>");
        }

        html.append("</div>");

        html.append("<div style='background: #333; color: #999; padding: 15px; border-radius: 0 0 10px 10px; text-align: center; font-size: 12px;'>");
        html.append("<p style='margin: 0;'>Harvest App - Sistem Manajemen Budidaya Ikan</p>");
        html.append("<p style='margin: 5px 0 0 0;'>Notification Type: ").append(notification.type().name()).append("</p>");
        html.append("</div>");

        html.append("</body></html>");

        return html.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
