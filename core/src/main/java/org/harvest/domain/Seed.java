package org.harvest.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record Seed(UUID id, UUID pondsId, String type, int quantity, LocalDate stockDate, UUID organizationId) {

    public Seed update(String type, int quantity, LocalDate stockDate) {
        return new Seed(id, pondsId, type, quantity, stockDate, organizationId);
    }
}
