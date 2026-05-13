package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

import java.util.UUID;

public record UpdateSupplierCommand (UserSession session, UUID supplierId, String name, String phone, String address, String email, String notes){
}
