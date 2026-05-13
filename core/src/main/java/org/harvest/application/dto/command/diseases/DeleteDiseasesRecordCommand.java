package org.harvest.application.dto.command;

import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record DeleteDiseasesRecordCommand(
        UserSession session,
        UUID id
) {
}
