package org.harvest.application.dto.command;

import org.harvest.application.port.outbound.security.UserSession;

public record CreateSupplierCommand(UserSession session, String name, String address, String phone, String email , String notes) {
}
