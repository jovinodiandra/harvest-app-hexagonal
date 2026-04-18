package org.harvest.application.port.outbound;

import org.harvest.domain.Organization;
import org.harvest.domain.Seed;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.UUID;

public interface SeedRepository {
    UUID nextId();
    void save(Seed seed);
    void delete(Seed seed);
    void Update(Seed seed);
    Seed findById(UUID id);
    List<Seed>findAllByOrganization(UUID organizationId, Pagination pagination);
}
