package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewReader;
import org.orury.domain.crew.domain.entity.Crew;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CrewReaderImpl implements CrewReader {
    private final CrewRepository crewRepository;

    @Override
    public Optional<Crew> findById(Long crewId) {
        return crewRepository.findById(crewId);
    }
}
