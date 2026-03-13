package org.harvest.application.port.outbound;

public interface PasswordSecurity {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
