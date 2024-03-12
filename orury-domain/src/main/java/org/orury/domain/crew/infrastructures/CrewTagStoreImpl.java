package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewTagStore;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewTag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CrewTagStoreImpl implements CrewTagStore {
    private final CrewTagRepository crewTagRepository;

    @Override
    public void addTags(Crew crew, List<String> tags) {
        tags.stream()
                .map(tag -> CrewTag.of(null, crew, tag))
                .forEach(crewTagRepository::save);
    }
}
