package org.orury.domain.admin.domain.dto;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("ROLE_USER", "일반 유저"),
    ADMIN("ROLE_ADMIN", "어드민 유저");

    private final String roleName;

    private final String description;

    RoleType(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }
}
