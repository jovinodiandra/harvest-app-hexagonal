package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateSupplierResult;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateSupplierResponse(UUID id, String name, String address, String phone, String email, String notes, LocalDateTime updatedAt) {

    public static UpdateSupplierResponse from(UpdateSupplierResult result) {
        return new UpdateSupplierResponse(result.id(), result.name(), result.address(), result.phone(), result.email(), result.notes(), result.updatedAt());
    }
}
