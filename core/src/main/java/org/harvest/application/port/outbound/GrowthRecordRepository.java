package org.harvest.application.port.outbound;

import org.harvest.domain.GrowthRecord;

import java.util.List;
import java.util.UUID;

public interface GrowthRecordRepository {
    UUID nextId();
    void save(GrowthRecord growthRecords);
    GrowthRecord findById(UUID id);
    void delete(GrowthRecord growthRecords);
    void update(GrowthRecord growthRecords);
    List<GrowthRecord> findAllByOrganizationId(UUID organizationId,int offset, int limit);
    List<GrowthRecord> findAllByPondsIdAndOrganizationId(UUID pondsId, UUID organizationId, int offset, int limit);
}
