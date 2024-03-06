package org.orury.domain.meeting.domain.dto;

import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link Meeting}
 */
public record MeetingDto(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int memberCount,
        int capacity,
        UserDto userDto,
        GymDto gymDto,
        CrewDto crewDto,
        Boolean isParticipated,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MeetingDto of(
            Long id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int memberCount,
            int capacity,
            UserDto userDto,
            GymDto gymDto,
            CrewDto crewDto,
            Boolean isParticipated,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new MeetingDto(
                id,
                startTime,
                endTime,
                memberCount,
                capacity,
                userDto,
                gymDto,
                crewDto,
                isParticipated,
                createdAt,
                updatedAt
        );
    }

    public static MeetingDto from(Meeting entity) {
        return MeetingDto.of(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getMemberCount(),
                entity.getCapacity(),
                UserDto.from(entity.getUser()),
                GymDto.from(entity.getGym()),
                CrewDto.from(entity.getCrew()),
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static MeetingDto from(Meeting entity, boolean isParticipated) {
        return MeetingDto.of(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getMemberCount(),
                entity.getCapacity(),
                UserDto.from(entity.getUser()),
                GymDto.from(entity.getGym()),
                CrewDto.from(entity.getCrew()),
                isParticipated,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Meeting toEntity() {
        return Meeting.of(
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
