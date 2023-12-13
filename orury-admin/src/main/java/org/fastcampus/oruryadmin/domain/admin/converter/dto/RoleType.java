package org.fastcampus.oruryadmin.domain.admin.converter.dto;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }
}
