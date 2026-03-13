package org.harvest.application.port.outbound;

import org.harvest.domain.FeedSchedule;

import java.util.List;
import java.util.UUID;

public interface FeedScheduleRepository {
    UUID nextId();
    void save(FeedSchedule feedSchedule);
    FeedSchedule findById(UUID id);
    void delete(FeedSchedule feedSchedule);
    void update(FeedSchedule feedSchedule);
    List<FeedSchedule> findByOrganizationId(UUID organizationId, int offset, int limit);
}
