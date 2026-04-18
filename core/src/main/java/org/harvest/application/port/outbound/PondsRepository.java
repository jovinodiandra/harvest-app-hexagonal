package org.harvest.application.port.outbound;

import org.harvest.domain.Ponds;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.UUID;

public interface PondsRepository {
    UUID nextId();
    void save(Ponds ponds);
    Ponds findById(UUID id);
    List<Ponds> findAllByOrganizationId(UUID organizationId, Pagination pagination);
    void delete(Ponds ponds);
    void update(Ponds ponds);
}
