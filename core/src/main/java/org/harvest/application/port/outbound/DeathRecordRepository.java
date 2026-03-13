package org.harvest.application.port.outbound;

import org.harvest.domain.DeathRecord;

import java.util.List;
import java.util.UUID;

public interface DeathRecordRepository {
    UUID nextId();
    void save( DeathRecord deathRecord);
    DeathRecord findById(UUID id);
    void delete(DeathRecord deathRecord);
    void update(DeathRecord deathRecord);
    List<DeathRecord> findAllByPondsId(UUID pondsId,int offset, int limit);
    List<DeathRecord> findAllByOrganizationId(UUID organizationId);
}
