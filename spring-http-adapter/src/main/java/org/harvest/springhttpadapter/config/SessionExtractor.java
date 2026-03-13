package org.harvest.springhttpadapter.config;

import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.shared.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionExtractor {
    private final SessionManager sessionManager;
    public SessionExtractor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public UserSession extract(String headers) {
        if (headers == null) {
            throw new AuthenticationException("Token not found");
        }
        if (headers.startsWith("Bearer ")) {
            String token = headers.substring(7);
            return sessionManager.getSession(token);
        }
        throw new AuthenticationException("Invalid token format");
    }

}
