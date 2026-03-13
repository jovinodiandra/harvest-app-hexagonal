package org.harvest.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record HarvestReminder(UUID id, UUID pondId, LocalDate reminderDate, LocalTime reminderTime, String notes ,UUID organizationId) {
    public HarvestReminder update(LocalDate reminderDate, LocalTime reminderTime, String notes) {
        return new HarvestReminder(id, pondId, reminderDate, reminderTime, notes, organizationId);
    }
}
