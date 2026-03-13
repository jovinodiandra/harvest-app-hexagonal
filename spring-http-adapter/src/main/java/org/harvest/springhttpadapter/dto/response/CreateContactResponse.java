package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateContactResult;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateContactResponse(UUID id, UUID supplierId, String name, String email, String phone, String address, String notes, LocalDateTime createdAt) {

    public static CreateContactResponse from(CreateContactResult result) {
        return new CreateContactResponse(result.id(), result.supplierId(), result.name(), result.email(), result.phone(), result.address(), result.notes(), result.createdAt());
    }
}
