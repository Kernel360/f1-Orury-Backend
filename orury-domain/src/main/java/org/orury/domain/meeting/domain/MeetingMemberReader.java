package org.orury.domain.meeting.domain;

import org.orury.domain.meeting.domain.entity.MeetingMember;

import java.util.List;

public interface MeetingMemberReader {
    boolean existsByMeetingIdAndUserId(Long meetingId, Long userId);

    List<MeetingMember> getOtherMeetingMembersByMeetingIdMaximum(Long meetingId, Long meetingCreatorId, int maximum);

    List<MeetingMember> getMeetingMembersByMeetingId(Long meetingId);
}
