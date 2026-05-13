package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateDeathRecordCommand (UserSession session, UUID pondsId, UUID deathRecordId, LocalDate recordDate, int deathCount, String notes){
}
