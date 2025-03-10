package org.springboot.api.library.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Admin"),
    USER("user");

    private final String role;

    Role(String role) {
        this.role = role;
    }

}