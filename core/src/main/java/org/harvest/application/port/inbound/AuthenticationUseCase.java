package org.harvest.application.port.inbound;

import org.harvest.application.port.outbound.Presenter;
import org.harvest.application.port.outbound.security.UserSession;

import java.util.Objects;

public abstract class AuthenticationUseCase<C, R> implements UseCase<C, R> {
    @Override
    public void process(C command, Presenter<R> presenter) {
        try {
            validateCommandNotNull(command);
            validateCommand(command);
            UserSession userSession = authenticate(command);
            R result = executeBusiness(command, userSession);
            presenter.presentSuccess(result);
        } catch (Exception e) {
            presenter.presentError(e);
        }
    }

    protected final void validateCommandNotNull(C command) {
        Objects.requireNonNull(command, "Command cannot be null");
    }

    protected abstract UserSession authenticate(C command);

    protected abstract R executeBusiness(C command, UserSession userSession);

    protected abstract void validateCommand(C command);
}
