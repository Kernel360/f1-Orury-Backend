package org.orury.domain.meeting.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.meeting.domain.MeetingStore;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingStoreImpl implements MeetingStore {
    private final MeetingRepository meetingRepository;

    @Override
    public void createMeeting(Meeting meeting) {
        meetingRepository.save(meeting);
    }

    @Override
    public void updateMeeting(Meeting meeting) {
        meetingRepository.save(meeting);
    }

    @Override
    public void deleteMeeting(Long meetingId) {
        meetingRepository.deleteById(meetingId);
    }
}
