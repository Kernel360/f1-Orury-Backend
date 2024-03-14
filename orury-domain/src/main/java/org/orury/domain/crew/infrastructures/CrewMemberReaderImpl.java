package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMemberReaderImpl implements CrewMemberReader {
    private final CrewMemberRepository crewMemberRepository;

    @Override
    public boolean existByCrewIdAndUserId(Long crewId, Long userId) {
        return crewMemberRepository.existsByCrewMemberPK_CrewIdAndCrewMemberPK_UserId(crewId, userId);
    }

    @Override
    public int countByUserId(Long userId) {
        return crewMemberRepository.countByCrewMemberPK_UserId(userId);
    }
}
