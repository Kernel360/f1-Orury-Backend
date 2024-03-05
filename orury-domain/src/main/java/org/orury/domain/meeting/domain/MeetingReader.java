package org.orury.domain.meeting.domain;

import org.orury.domain.meeting.domain.entity.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingReader {
    Optional<Meeting> findById(Long meetingId);

    List<Meeting> getNotEndedMeetingsByCrewId(Long crewId);

    List<Meeting> getEndedMeetingsByCrewId(Long crewId);
}
