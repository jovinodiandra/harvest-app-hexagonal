package org.harvest.notification.adapter;

/**
 * SMTP configuration for email notifications.
 */
public record SmtpConfig(
        String host,
        int port,
        String username,
        String password,
        String fromEmail,
        String fromName,
        boolean useTls,
        boolean useAuth
) {
    public static SmtpConfig create(
            String host,
            int port,
            String username,
            String password,
            String fromEmail,
            String fromName
    ) {
        return new SmtpConfig(host, port, username, password, fromEmail, fromName, true, true);
    }

    public static SmtpConfig gmail(String username, String password, String fromName) {
        return new SmtpConfig(
                "smtp.gmail.com",
                587,
                username,
                password,
                username,
                fromName,
                true,
                true
        );
    }

    public static SmtpConfig mailtrap(String username, String password) {
        return new SmtpConfig(
                "smtp.mailtrap.io",
                2525,
                username,
                password,
                "noreply@harvest.app",
                "Harvest App",
                true,
                true
        );
    }
}
