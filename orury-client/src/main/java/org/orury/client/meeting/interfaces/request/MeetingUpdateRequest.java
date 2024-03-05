package org.orury.client.meeting.interfaces.request;

import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.meeting.domain.dto.MeetingDto;

import java.time.LocalDateTime;

public record MeetingUpdateRequest(
        Long meetingId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int capacity,
        Long gymId
) {
    public MeetingDto toDto(MeetingDto meetingDto, GymDto gymDto) {
        return MeetingDto.of(
                meetingDto.id(),
                startTime,
                endTime,
                meetingDto.memberCount(),
                capacity,
                meetingDto.userDto(),
                gymDto,
                meetingDto.crewDto(),
                null,
                meetingDto.createdAt(),
                null
        );
    }
}
