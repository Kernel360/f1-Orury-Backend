package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewTagReader;
import org.orury.domain.crew.domain.entity.CrewTag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CrewTagReaderImpl implements CrewTagReader {
    private final CrewTagRepository crewTagRepository;

    @Override
    public List<String> getTagsByCrewId(Long crewId) {
        return crewTagRepository.findCrewTagByCrewId(crewId)
                .stream().map(CrewTag::getTag).toList();
    }
}
