package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewStore;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewStoreImpl implements CrewStore {
    private final CrewRepository crewRepository;
}
