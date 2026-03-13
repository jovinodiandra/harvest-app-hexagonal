package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateFeedScheduleCommand (UserSession session, UUID pondsId,UUID feedId, String feedType, BigDecimal feedAmount, LocalTime feedTime){
}
