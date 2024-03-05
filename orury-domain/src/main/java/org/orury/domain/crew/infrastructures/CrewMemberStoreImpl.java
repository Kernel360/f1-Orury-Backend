package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMemberStore;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMemberStoreImpl implements CrewMemberStore {
    private final CrewMemberRepository crewMemberRepository;
}
