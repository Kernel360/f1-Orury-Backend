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
        boolean isDeleted,
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int minAge,
        int maxAge,
        CrewGender gender,
        boolean permissionRequired,
        String question,
        boolean answerRequired
) {
    public static CrewDto of(
            Long id,
            String name,
            int memberCount,
            int capacity,
            String region,
            String description,
            String icon,
            boolean isDeleted,
            UserDto userDto,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int minAge,
            int maxAge,
            CrewGender gender,
            boolean permissionRequired,
            String question,
            boolean answerRequired
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
                updatedAt,
                minAge,
                maxAge,
                gender,
                permissionRequired,
                question,
                answerRequired
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
                entity.isDeleted(),
                UserDto.from(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getMinAge(),
                entity.getMaxAge(),
                entity.getGender(),
                entity.isPermissionRequired(),
                entity.getQuestion(),
                entity.isAnswerRequired()
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
                updatedAt,
                minAge,
                maxAge,
                gender,
                permissionRequired,
                question,
                answerRequired
        );
    }
}

