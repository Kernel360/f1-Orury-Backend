package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewTagStore;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewTag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

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

    @Override
    public void updateTags(List<String> oldTags, List<String> newTags, Crew crew) {
        if (Objects.equals(oldTags, newTags))
            return;
        crewTagRepository.deleteAllByCrewId(crew.getId());
        newTags.stream()
                .map(tag -> CrewTag.of(null, crew, tag))
                .forEach(crewTagRepository::save);
    }
}
