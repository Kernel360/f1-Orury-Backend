package org.orury.domain.meeting.domain;

import org.orury.domain.meeting.domain.dto.MeetingMemberDto;

public interface MeetingMemberStore {
    void addMember(MeetingMemberDto meetingMemberDto);

    void removeMember(MeetingMemberDto meetingMemberDto);

    void removeAllByUserIdAndCrewId(Long userId, Long crewId);
}
