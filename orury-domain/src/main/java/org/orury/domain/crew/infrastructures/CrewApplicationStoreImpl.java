package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewApplicationStore;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewApplicationStoreImpl implements CrewApplicationStore {
    private final CrewApplicationRepository crewApplicationRepository;
}
