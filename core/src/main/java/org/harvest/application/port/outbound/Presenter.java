package org.harvest.application.port.outbound;

public interface Presenter<R> {
    void presentSuccess(R result);

    void presentError(Exception error);
}
