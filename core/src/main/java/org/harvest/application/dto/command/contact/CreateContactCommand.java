package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record CreateContactCommand (UserSession session, UUID supplierId, String name, String email, String phone, String address, String notes){
}
