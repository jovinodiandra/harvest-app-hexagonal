package org.harvest.application.dto.result;

import java.util.UUID;

public record UpdateContactResult(UUID id, String name, String address, String phone, String email, String notes) {
}
