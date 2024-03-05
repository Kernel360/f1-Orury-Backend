package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMemberReaderImpl implements CrewMemberReader {
    private final CrewMemberRepository crewMemberRepository;
}
