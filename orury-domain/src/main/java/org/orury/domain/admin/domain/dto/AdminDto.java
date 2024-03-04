package org.orury.domain.admin.domain.dto;

import org.orury.domain.admin.domain.entity.Admin;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link Admin}
 */
public record AdminDto(
        Long id,
        String name,
        String email,
        String password,
        Set<RoleType> roleTypes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdminDto of(
            Long id,
            String name,
            String email,
            String password,
            Set<RoleType> roleTypes
    ) {
        return AdminDto.of(
                id,
                name,
                email,
                password,
                roleTypes,
                null,
                null
        );
    }

    public static AdminDto of(
            Long id,
            String name,
            String email,
            String password,
            Set<RoleType> roleTypes,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new AdminDto(
                id,
                name,
                email,
                password,
                roleTypes,
                createdAt,
                updatedAt
        );
    }

    public static AdminDto from(Admin entity) {
        return AdminDto.of(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRoleTypes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Admin toEntity() {
        return Admin.of(
                name,
                email,
                password,
                roleTypes
        );
    }
}