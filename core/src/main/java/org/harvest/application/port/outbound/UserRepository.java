package org.harvest.application.port.outbound;

import org.harvest.domain.User;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    UUID nextId();

    boolean existsByEmail(String email);

    List<User> findAllByOrganizationId(UUID organizationId, Pagination pagination);

    User findByEmail(String email);

    void save(User user);

    void delete(User user);

    User findById(UUID id);

    User updateUser(User user);

    User findByEmailAndPassword(String email, String password);
}
