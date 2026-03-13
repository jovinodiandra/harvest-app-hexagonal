package org.harvest.application.usecase;

import org.harvest.application.dto.query.LogoutQuery;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.application.port.outbound.security.UserSession;

public class LogoutUseCase extends AuthenticationUseCase<LogoutQuery, DefaultResult> {
    private final SessionManager sessionManager;

    public LogoutUseCase(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    protected UserSession authenticate(LogoutQuery command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(LogoutQuery command, UserSession userSession) {
        sessionManager.invalidateAllSessions(userSession.getUserId());
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(LogoutQuery command) {

    }
}
