package org.orury.domain.meeting.domain;

import org.orury.domain.meeting.domain.entity.MeetingMember;

public interface MeetingMemberStore {
    void addMember(MeetingMember meetingMember);

    void removeMember(MeetingMember meetingMember);

    void removeAllByUserIdAndCrewId(Long userId, Long crewId);
}
