package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMeetingMemberStore;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMeetingMemberStoreImpl implements CrewMeetingMemberStore {
    private final CrewMeetingMemberRepository crewMeetingMemberRepository;
}
