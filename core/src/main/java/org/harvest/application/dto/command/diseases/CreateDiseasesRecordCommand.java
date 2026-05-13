package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDiseasesRecordCommand(
        UserSession session,
        UUID pondId,
        String diseaseName,
        String symptoms,
        int infectedFishCount,
        LocalDate diseaseDate,
        String notes
) {
}
