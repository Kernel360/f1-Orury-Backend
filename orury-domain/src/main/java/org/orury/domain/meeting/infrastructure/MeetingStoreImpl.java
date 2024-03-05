package org.orury.domain.meeting.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.meeting.domain.MeetingStore;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingStoreImpl implements MeetingStore {
    private final MeetingRepository meetingRepository;
}
