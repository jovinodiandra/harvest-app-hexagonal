package org.harvest.application.port.outbound;

import org.harvest.domain.Contact;

import java.util.List;
import java.util.UUID;

public interface ContactRepository {
    UUID nextId();
    void save(Contact contact);

    void delete(Contact contact);

    void update(Contact contact);

    Contact findById(UUID id);

    List<Contact> findByFilter(UUID supplierId, String name, int offset, int limit);

}
