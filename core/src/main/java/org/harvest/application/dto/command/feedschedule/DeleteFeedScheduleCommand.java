package org.harvest.application.dto.command.feedschedule;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record DeleteFeedScheduleCommand (UserSession session, UUID feedScheduleId){
}
