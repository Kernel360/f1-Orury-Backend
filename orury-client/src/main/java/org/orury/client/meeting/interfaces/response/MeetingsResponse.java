package org.orury.client.meeting.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.domain.meeting.domain.dto.MeetingDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record MeetingsResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime startTime,
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
            LocalDateTime startTime,
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
                startTime,
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
                meetingDto.startTime(),
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
