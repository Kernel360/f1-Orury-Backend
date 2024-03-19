package org.orury.domain.crew.domain.dto;

import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link CrewApplication}
 */
public record CrewApplicationDto(
        CrewApplicationPK crewApplicationPK,
        String answer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CrewApplicationDto of(
            CrewApplicationPK crewApplicationPK,
            String answer,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CrewApplicationDto(
                crewApplicationPK,
                answer,
                createdAt,
                updatedAt
        );
    }

    public static CrewApplicationDto from(CrewApplication crewApplication) {
        return CrewApplicationDto.of(
                crewApplication.getCrewApplicationPK(),
                crewApplication.getAnswer(),
                crewApplication.getCreatedAt(),
                crewApplication.getUpdatedAt()
        );
    }

    public CrewApplication toEntity() {
        return CrewApplication.of(
                crewApplicationPK,
                answer,
                createdAt,
                updatedAt
        );
    }

    public static CrewApplicationDto of(CrewDto crewDto, UserDto userDto, String answer) {
        CrewApplicationPK crewApplicationPK = CrewApplicationPK.of(userDto.id(), crewDto.id());
        return CrewApplicationDto.of(
                crewApplicationPK,
                answer,
                null,
                null
        );
    }
}
