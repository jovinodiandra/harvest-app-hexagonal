package org.harvest.domain;

import java.util.UUID;

public record Contact(UUID id, UUID supplierId, String name, String address, String phone, String email, String notes) {
    public Contact update(String name, String address, String phone, String email, String notes) {
        return new Contact(id, supplierId, name, address, phone, email, notes);
    }
}
