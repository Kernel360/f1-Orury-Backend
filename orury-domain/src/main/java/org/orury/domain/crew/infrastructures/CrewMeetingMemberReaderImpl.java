package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMeetingMemberReader;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMeetingMemberReaderImpl implements CrewMeetingMemberReader {
    private final CrewMeetingMemberRepository crewMeetingMemberRepository;
}
