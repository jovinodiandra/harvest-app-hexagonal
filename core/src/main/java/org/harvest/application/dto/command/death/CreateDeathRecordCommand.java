package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDeathRecordCommand (UserSession session, UUID pondsId, LocalDate recordDate, int deathCount, String notes){
}
