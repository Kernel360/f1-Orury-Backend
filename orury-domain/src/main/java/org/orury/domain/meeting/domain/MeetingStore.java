package org.orury.domain.meeting.domain;

import org.orury.domain.meeting.domain.entity.Meeting;

public interface MeetingStore {
    Meeting createMeeting(Meeting meeting);

    void updateMeeting(Meeting meeting);

    void deleteMeeting(Long meetingId);
}
