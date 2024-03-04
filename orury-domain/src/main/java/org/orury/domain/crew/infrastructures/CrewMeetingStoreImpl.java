package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMeetingStore;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMeetingStoreImpl implements CrewMeetingStore {
    private final CrewMeetingRepository crewMeetingRepository;
}
