package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewApplicationReader;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewApplicationReaderImpl implements CrewApplicationReader {
    private final CrewApplicationRepository crewApplicationRepository;
}
