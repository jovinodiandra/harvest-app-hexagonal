package org.harvest.application.port.outbound;

import org.harvest.domain.DeathRecord;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.UUID;

public interface DeathRecordRepository {
    UUID nextId();
    void save( DeathRecord deathRecord);
    DeathRecord findById(UUID id);
    void delete(DeathRecord deathRecord);
    void update(DeathRecord deathRecord);
    List<DeathRecord> findAllByPondsId(UUID pondsId, Pagination pagination);
    List<DeathRecord> findAllByOrganizationId(UUID organizationId);
}
