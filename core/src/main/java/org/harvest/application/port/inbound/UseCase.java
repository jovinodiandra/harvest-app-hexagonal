package org.harvest.application.port.inbound;

import org.harvest.application.port.outbound.Presenter;

public interface UseCase<C, R> {
    void process(C command, Presenter<R> result);
}
