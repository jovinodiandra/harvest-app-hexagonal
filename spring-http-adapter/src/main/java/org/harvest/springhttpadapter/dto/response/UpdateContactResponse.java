package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateContactResult;

import java.util.UUID;

public record UpdateContactResponse(UUID id, String name, String address, String phone, String email, String notes) {

    public static UpdateContactResponse from(UpdateContactResult result) {
        return new UpdateContactResponse(result.id(), result.name(), result.address(), result.phone(), result.email(), result.notes());
    }
}
