package org.orury.domain.meeting.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.meeting.domain.MeetingReader;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MeetingReaderImpl implements MeetingReader {
    private final MeetingRepository meetingRepository;

    @Override
    public Optional<Meeting> findById(Long meetingId) {
        return meetingRepository.findById(meetingId);
    }

    @Override
    public List<Meeting> getNotStartedMeetingsByCrewId(Long crewId) {
        return meetingRepository.findByCrew_IdAndStartTimeAfterOrderByIdDesc(crewId, LocalDateTime.now());
    }

    @Override
    public List<Meeting> getStartedMeetingsByCrewId(Long crewId) {
        return meetingRepository.findByCrew_IdAndStartTimeBeforeOrderByIdDesc(crewId, LocalDateTime.now());
    }
}
