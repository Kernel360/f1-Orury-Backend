package org.orury.domain.crew.domain.dto;

import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link Crew}
 */
public record CrewDto(
        Long id,
        String name,
        int memberCount,
        int capacity,
        String region,
        String description,
        String icon,
        int isDeleted,
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CrewDto of(
            Long id,
            String name,
            int memberCount,
            int capacity,
            String region,
            String description,
            String icon,
            int isDeleted,
            UserDto userDto,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CrewDto(
                id,
                name,
                memberCount,
                capacity,
                region,
                description,
                icon,
                isDeleted,
                userDto,
                createdAt,
                updatedAt
        );
    }

    public static CrewDto from(Crew entity) {
        return CrewDto.of(
                entity.getId(),
                entity.getName(),
                entity.getMemberCount(),
                entity.getCapacity(),
                entity.getRegion(),
                entity.getDescription(),
                entity.getIcon(),
                entity.getIsDeleted(),
                UserDto.from(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Crew toEntity() {
        return Crew.of(
                id,
                name,
                memberCount,
                capacity,
                region,
                description,
                icon,
                isDeleted,
                userDto.toEntity(),
                createdAt,
                updatedAt
        );
    }
}

