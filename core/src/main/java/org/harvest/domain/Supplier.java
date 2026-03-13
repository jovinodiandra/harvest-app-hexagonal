package org.harvest.domain;

import java.util.UUID;

public record Supplier(UUID id, String name, String address, String phone, String email, String notes, UUID organizationId) {
    public Supplier update(String name, String address, String phone, String email, String notes) {
        return new Supplier(id, name, address, phone, email, notes , organizationId);
    }
}
