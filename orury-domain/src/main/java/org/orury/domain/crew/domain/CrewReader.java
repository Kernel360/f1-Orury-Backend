package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.Crew;

import java.util.Optional;

public interface CrewReader {
    Optional<Crew> findById(Long crewId);
}
