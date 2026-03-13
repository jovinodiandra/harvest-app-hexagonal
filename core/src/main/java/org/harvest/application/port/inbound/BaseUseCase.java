package org.harvest.application.port.inbound;

import org.harvest.application.port.outbound.Presenter;

import java.util.Objects;


//C untuk command, R untuk result
//ini  namanya template pattern
public abstract class BaseUseCase<C, R> implements UseCase<C, R> {
    @Override
    public void process(C command, Presenter<R> presenter) {
        try {
            validateCommandNotNull(command);
            validateCommand(command);
            R result = executeBusiness(command);
            presenter.presentSuccess(result);

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    protected final void validateCommandNotNull(C command) {
        Objects.requireNonNull(command, "Command cannot be null");
    }

    protected abstract R executeBusiness(C command);

    protected abstract void validateCommand(C command);
}
