package org.orury.domain.crew.domain.dto;

import org.apache.logging.log4j.util.Strings;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.global.domain.Region;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Crew}
 */
public record CrewDto(
        Long id,
        String name,
        int memberCount,
        int capacity,
        Region region,
        String description,
        String icon,
        CrewStatus status,
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int minAge,
        int maxAge,
        CrewGender gender,
        boolean permissionRequired,
        String question,
        boolean answerRequired,
        List<String> tags
) {
    public static CrewDto of(
            Long id,
            String name,
            int memberCount,
            int capacity,
            Region region,
            String description,
            String icon,
            CrewStatus status,
            UserDto userDto,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int minAge,
            int maxAge,
            CrewGender gender,
            Boolean permissionRequired,
            String question,
            Boolean answerRequired,
            List<String> tags
    ) {
        return new CrewDto(
                id,
                name,
                memberCount,
                capacity,
                region,
                description,
                icon,
                status,
                userDto,
                createdAt,
                updatedAt,
                minAge,
                maxAge,
                gender,
                permissionRequired,
                question,
                answerRequired,
                tags
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
                entity.getStatus(),
                UserDto.from(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getMinAge(),
                entity.getMaxAge(),
                entity.getGender(),
                entity.isPermissionRequired(),
                entity.getQuestion(),
                entity.isAnswerRequired(),
                null
        );
    }

    public static CrewDto from(Crew entity, List<String> tags) {
        return CrewDto.of(
                entity.getId(),
                entity.getName(),
                entity.getMemberCount(),
                entity.getCapacity(),
                entity.getRegion(),
                entity.getDescription(),
                entity.getIcon(),
                entity.getStatus(),
                UserDto.from(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getMinAge(),
                entity.getMaxAge(),
                entity.getGender(),
                entity.isPermissionRequired(),
                entity.getQuestion(),
                entity.isAnswerRequired(),
                tags
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
                status,
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

    @Value("${cloud.aws.s3.default-image.crew}")
    private static String CREW_DEFAULT_IMAGE;

    public Crew toEntity(String icon) {
        if (Strings.isBlank(icon)) icon = CREW_DEFAULT_IMAGE;

        return Crew.of(
                id,
                name,
                memberCount,
                capacity,
                region,
                description,
                icon,
                status,
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

