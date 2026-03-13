package org.harvest.application.port.outbound;

import org.harvest.domain.Ponds;

import java.util.List;
import java.util.UUID;

public interface PondsRepository {
    UUID nextId();
    void save(Ponds ponds);
    Ponds findById(UUID id);
    List<Ponds> findAllByOrganizationId(UUID organizationId,int offset, int limit);
    void delete(Ponds ponds);
    void update(Ponds ponds);
}
