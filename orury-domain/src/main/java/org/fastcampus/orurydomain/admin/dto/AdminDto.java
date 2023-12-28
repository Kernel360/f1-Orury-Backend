package org.fastcampus.orurydomain.admin.dto;

import org.fastcampus.orurydomain.admin.db.model.Admin;

import java.time.LocalDateTime;

/**
 * DTO for {@link Admin}
 */
public record AdminDto(
        Long id,
        String name,
        String email,
        String password,
        RoleType role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdminDto of(
            String name,
            String email,
            String password,
            RoleType role
    ) {
        return AdminDto.of(
                null,
                name,
                email,
                password,
                role,
                null,
                null
        );
    }

    public static AdminDto of(
            Long id,
            String name,
            String email,
            String password,
            RoleType role,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new AdminDto(
                id,
                name,
                email,
                password,
                role,
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
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Admin toEntity() {
        return Admin.of(
                name,
                email,
                password,
                role
        );
    }
}