package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateDiseasesRecordCommand(
        UserSession session,
        UUID id,
        UUID pondId,
        String diseaseName,
        String symptoms,
        int infectedFishCount,
        LocalDate diseaseDate,
        String notes
) {
}
