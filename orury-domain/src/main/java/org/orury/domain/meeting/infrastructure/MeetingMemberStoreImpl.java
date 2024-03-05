package org.orury.domain.meeting.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingMemberStoreImpl implements MeetingMemberStore {
    private final MeetingMemberRepository meetingMemberRepository;
}
