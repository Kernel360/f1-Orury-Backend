package org.orury.domain.crew.domain.dto;

import org.orury.domain.crew.domain.entity.CrewMeeting;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link CrewMeeting}
 */
public record CrewMeetingDto(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int memberCount,
        int capacity,
        UserDto userDto,
        GymDto gymDto,
        CrewDto crewDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CrewMeetingDto of(
            Long id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int memberCount,
            int capacity,
            UserDto userDto,
            GymDto gymDto,
            CrewDto crewDto,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CrewMeetingDto(
                id,
                startTime,
                endTime,
                memberCount,
                capacity,
                userDto,
                gymDto,
                crewDto,
                createdAt,
                updatedAt
        );
    }

    public static CrewMeetingDto from(CrewMeeting entity) {
        return CrewMeetingDto.of(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getMemberCount(),
                entity.getCapacity(),
                UserDto.from(entity.getUser()),
                GymDto.from(entity.getGym()),
                CrewDto.from(entity.getCrew()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public CrewMeeting toEntity() {
        return CrewMeeting.of(
                id,
                startTime,
                endTime,
                memberCount,
                capacity,
                userDto.toEntity(),
                gymDto.toEntity(),
                crewDto.toEntity(),
                createdAt,
                updatedAt
        );
    }
}
