package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewStore;
import org.orury.domain.crew.domain.entity.Crew;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewStoreImpl implements CrewStore {
    private final CrewRepository crewRepository;

    @Override
    public Crew save(Crew crew) {
        return crewRepository.save(crew);
    }
}
