package org.orury.domain.meeting.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.meeting.domain.MeetingMemberReader;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingMemberReaderImpl implements MeetingMemberReader {
    private final MeetingMemberRepository meetingMemberRepository;
}
