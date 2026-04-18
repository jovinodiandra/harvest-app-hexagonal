package org.harvest.application.port.outbound;

import org.harvest.domain.WatterQuality;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.UUID;

public interface WatterQualityRepository {
    UUID nextId();
    void save(WatterQuality watterQuality);
    WatterQuality findById(UUID id);
    void delete(WatterQuality watterQuality);
    void update(WatterQuality watterQuality);
    List<WatterQuality> findAllByPondsId(UUID pondsId, Pagination pagination);
}
