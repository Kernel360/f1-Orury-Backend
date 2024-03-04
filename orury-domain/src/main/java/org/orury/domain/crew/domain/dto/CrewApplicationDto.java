package org.orury.domain.crew.domain.dto;

import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;

import java.time.LocalDateTime;

/**
 * DTO for {@link CrewApplication}
 */
public record CrewApplicationDto(
        CrewApplicationPK crewApplicationPK,
        String description,
        int isDeclined,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CrewApplicationDto of(
            CrewApplicationPK crewApplicationPK,
            String description,
            int isDeclined,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CrewApplicationDto(
                crewApplicationPK,
                description,
                isDeclined,
                createdAt,
                updatedAt
        );
    }

    public static CrewApplicationDto from(CrewApplication crewApplication) {
        return CrewApplicationDto.of(
                crewApplication.getCrewApplicationPK(),
                crewApplication.getDescription(),
                crewApplication.getIsDeclined(),
                crewApplication.getCreatedAt(),
                crewApplication.getUpdatedAt()
        );
    }

    public CrewApplication toEntity() {
        return CrewApplication.of(
                crewApplicationPK,
                description,
                isDeclined,
                createdAt,
                updatedAt
        );
    }
}
