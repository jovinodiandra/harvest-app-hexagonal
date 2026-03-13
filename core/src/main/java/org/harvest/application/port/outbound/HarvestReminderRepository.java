package org.harvest.application.port.outbound;

import org.harvest.domain.HarvestReminder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HarvestReminderRepository {
    UUID nextId();

    void save(HarvestReminder harvestReminder);

    void delete(HarvestReminder harvestReminder);

    HarvestReminder findById(UUID id);

    List<HarvestReminder> findByFilters(UUID organizationId, UUID pondId, LocalDate date);

    void update(HarvestReminder harvestReminder);
}
