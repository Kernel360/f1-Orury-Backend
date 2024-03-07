package org.orury.client.meeting.interfaces.response;

import org.orury.domain.user.domain.dto.UserDto;

import java.util.Objects;

public record MeetingMembersResponse(
        Long id,
        String nickname,
        String profileImage,
        boolean isMeetingCreator,
        boolean isMe
) {
    public static MeetingMembersResponse of(
            Long id,
            String nickname,
            String profileImage,
            boolean isMeetingCreator,
            boolean isMe
    ) {
        return new MeetingMembersResponse(
                id,
                nickname,
                profileImage,
                isMeetingCreator,
                isMe
        );
    }

    public static MeetingMembersResponse of(UserDto userDto, Long userId, Long meetingCreatorId) {
        return MeetingMembersResponse.of(
                userDto.id(),
                userDto.nickname(),
                userDto.profileImage(),
                Objects.equals(meetingCreatorId, userDto.id()),
                Objects.equals(userId, userDto.id())
        );
    }
}
