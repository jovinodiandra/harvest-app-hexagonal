package org.harvest.domain;

import org.harvest.application.dto.value.Role;

import java.util.UUID;

public record User(UUID id, String name, String email, String password, UUID organizationId, Role role) {
    public User updateName(String name) {
        return new User(id, name, email, password, organizationId, role);
    }

    public User UpdateRoleAndName(Role role, String name) {
        Role newRole = role != null ? role : this.role;
        String newName = name != null ? name : this.name;
        return new User(id, newName, email, password, organizationId, newRole);
    }
}
