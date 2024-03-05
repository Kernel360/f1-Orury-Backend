package org.orury.client.meeting.interfaces.request;

import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

public record MeetingCreateRequest(
        LocalDateTime startTime,
        LocalDateTime endTime,
        int capacity,
        Long gymId,
        Long crewId
) {
    public static MeetingCreateRequest of(
            LocalDateTime startTime,
            LocalDateTime endTime,
            int capacity,
            Long gymId,
            Long crewId
    ) {
        return new MeetingCreateRequest(
                startTime,
                endTime,
                capacity,
                gymId,
                crewId
        );
    }

    public MeetingDto toDto(UserDto userDto, GymDto gymDto, CrewDto crewDto) {
        return MeetingDto.of(
                null,
                startTime,
                endTime,
                0,
                capacity,
                userDto,
                gymDto,
                crewDto,
                null,
                null,
                null
        );
    }
}
