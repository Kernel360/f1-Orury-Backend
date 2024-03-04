package org.orury.domain.meeting.domain.dto;

import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.meeting.domain.entity.MeetingMemberPK;

/**
 * DTO for {@link MeetingMember}
 */
public record MeetingMemberDto(
        MeetingMemberPK meetingMemberPK
) {
    public static MeetingMemberDto of(MeetingMemberPK meetingMemberPK) {
        return new MeetingMemberDto(meetingMemberPK);
    }

    public static MeetingMemberDto from(MeetingMember meetingMember) {
        return MeetingMemberDto.of(meetingMember.getMeetingMemberPK());
    }

    public MeetingMember toEntity() {
        return MeetingMember.of(meetingMemberPK);
    }
}
