package org.harvest.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateSupplierResult (UUID id, String name, String address, String phone, String email, String notes, LocalDateTime updatedAt){
}
