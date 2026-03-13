package org.harvest.application.dto.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    OWNER("owner"), ADMIN("admin"), USER("user");
    private final String description;

    Role(String roles) {
        this.description = roles;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static Role fromDescription(String value) {
        if (value == null || value.isBlank()) return null;
        String v = value.trim().toLowerCase();
        for (Role r : values()) {
            if (r.description.equals(v)) return r;
        }
        return Role.valueOf(value.toUpperCase());
    }
}
