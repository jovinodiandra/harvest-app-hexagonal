package org.harvest.application.port.outbound;

import org.harvest.domain.Supplier;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.UUID;

public interface SupplierRepository {
    UUID nextId();
    void save(Supplier supplier);
    void delete(Supplier supplier);
    void update(Supplier supplier);
    Supplier findById(UUID id);
    List<Supplier> findAllByOrganization(UUID organizationId, Pagination pagination);
    List<Supplier> findByName(UUID organizationId, String name, Pagination pagination);

}
