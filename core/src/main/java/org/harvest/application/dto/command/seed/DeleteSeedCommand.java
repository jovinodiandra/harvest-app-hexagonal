package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record DeleteSeedCommand (UserSession session, UUID seedId){
}
