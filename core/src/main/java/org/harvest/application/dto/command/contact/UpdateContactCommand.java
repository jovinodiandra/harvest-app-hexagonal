package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record UpdateContactCommand (UserSession session, UUID contactId, String name, String address, String phone, String email, String notes){
}
