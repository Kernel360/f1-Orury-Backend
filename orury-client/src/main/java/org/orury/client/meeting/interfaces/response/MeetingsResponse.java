package org.orury.client.meeting.interfaces.response;

import org.orury.domain.meeting.domain.dto.MeetingDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record MeetingsResponse(
        LocalDate date,
        Long gymId,
        String gymName,
        int memberCount,
        int capacity,
        String meetingImage,
        List<String> userImages,
        boolean isCreator,
        boolean isParticipated
) {
    public static MeetingsResponse of(
            LocalDate date,
            Long gymId,
            String gymName,
            int memberCount,
            int capacity,
            String meetingImage,
            List<String> userImages,
            boolean isCreator,
            boolean isParticipated
    ) {
        return new MeetingsResponse(
                date,
                gymId,
                gymName,
                memberCount,
                capacity,
                meetingImage,
                userImages,
                isCreator,
                isParticipated
        );
    }

    public static MeetingsResponse of(
            MeetingDto meetingDto,
            List<String> userImages,
            Long userId
    ) {
        return new MeetingsResponse(
                meetingDto.startTime().toLocalDate(),
                meetingDto.gymDto().id(),
                meetingDto.gymDto().name(),
                meetingDto.memberCount(),
                meetingDto.capacity(),
                meetingDto.gymDto().images().get(0),
                userImages,
                Objects.equals(meetingDto.userDto().id(), userId),
                meetingDto.isParticipated()
        );
    }
}
