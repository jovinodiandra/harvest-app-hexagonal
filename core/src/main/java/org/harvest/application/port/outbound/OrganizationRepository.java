package org.harvest.application.port.outbound;

import org.harvest.domain.Organization;

import java.util.UUID;

public interface OrganizationRepository {
    UUID nextId();

    void save(Organization organization);

    Organization findByName(String name);

    Organization findById(UUID id);
}
