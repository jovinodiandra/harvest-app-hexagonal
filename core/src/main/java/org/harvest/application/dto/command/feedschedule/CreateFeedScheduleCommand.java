package org.harvest.application.dto.command.feedschedule;

import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record CreateFeedScheduleCommand (UserSession session, UUID pondsId,String pondName, String feedType, BigDecimal feedAmount, LocalTime feedTime){
}
