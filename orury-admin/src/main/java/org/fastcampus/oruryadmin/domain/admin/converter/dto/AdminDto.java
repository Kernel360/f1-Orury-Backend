package org.fastcampus.oruryadmin.domain.admin.converter.dto;

import org.fastcampus.oruryadmin.domain.admin.db.model.Admin;

import java.time.LocalDateTime;

/**
 * DTO for {@link Admin}
 */
public record AdminDto(
        Long id,
        String name,
        String email,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdminDto of(
            Long id,
            String name,
            String email,
            String password,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new AdminDto(
                id,
                name,
                email,
                password,
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
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Admin toEntity() {
        return Admin.of(
                name,
                email,
                password
        );
    }
}