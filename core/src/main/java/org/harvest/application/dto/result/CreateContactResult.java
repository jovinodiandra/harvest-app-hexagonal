package org.harvest.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateContactResult (UUID id, UUID supplierId, String name, String email, String phone, String address, String notes, LocalDateTime createdAt){
}
