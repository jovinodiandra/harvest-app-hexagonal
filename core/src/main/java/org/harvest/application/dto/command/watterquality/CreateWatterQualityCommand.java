package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateWatterQualityCommand (UserSession session, UUID pondsId, LocalDate recordDate, BigDecimal ph, BigDecimal temperature, BigDecimal dissolvedOxygen){
}
