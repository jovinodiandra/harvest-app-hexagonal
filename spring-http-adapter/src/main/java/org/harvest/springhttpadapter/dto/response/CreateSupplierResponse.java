package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateSupplierResult;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSupplierResponse(UUID id, String name, String address, String phone, String email, String notes, LocalDateTime createdAt) {

    public static CreateSupplierResponse from(CreateSupplierResult result) {
        return new CreateSupplierResponse(result.id(), result.name(), result.address(), result.phone(), result.email(), result.notes(), result.createdAt());
    }
}
