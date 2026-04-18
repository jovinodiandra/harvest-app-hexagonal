package org.harvest.application.port.outbound;

import org.harvest.domain.GrowthRecord;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.UUID;

public interface GrowthRecordRepository {
    UUID nextId();
    void save(GrowthRecord growthRecords);
    GrowthRecord findById(UUID id);
    void delete(GrowthRecord growthRecords);
    void update(GrowthRecord growthRecords);
    List<GrowthRecord> findAllByOrganizationId(UUID organizationId, Pagination pagination);
    List<GrowthRecord> findAllByPondsIdAndOrganizationId(UUID pondsId, UUID organizationId, Pagination pagination);
}
