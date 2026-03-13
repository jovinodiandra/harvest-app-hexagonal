package org.harvest.application.port.outbound;

import org.harvest.domain.Supplier;

import java.util.List;
import java.util.UUID;

public interface SupplierRepository {
    UUID nextId();
    void save(Supplier supplier);
    void delete(Supplier supplier);
    void update(Supplier supplier);
    Supplier findById(UUID id);
    List<Supplier> findAllByOrganization(UUID organizationId, int offset, int limit);
    List<Supplier> findByName(UUID organizationId, String name, int offset, int limit);

}
