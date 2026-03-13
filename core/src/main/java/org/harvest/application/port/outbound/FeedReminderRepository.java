package org.harvest.application.port.outbound;

import org.harvest.domain.FeedReminder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FeedReminderRepository
{
    UUID nextId();
    void save(FeedReminder feedReminder);
    void delete(FeedReminder feedReminder);
    FeedReminder findById(UUID id);
    List<FeedReminder> findByFilters(UUID organizationId, UUID pondId, LocalDate date);
    void update(FeedReminder feedReminder);
}
