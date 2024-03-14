package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<CrewMember> getOtherCrewMembersByCrewIdMaximum(Long crewId, Long crewCreatorId, int maximum) {
        return crewMemberRepository.findByCrewMemberPK_CrewIdAndCrewMemberPK_UserIdNot(crewId, crewCreatorId, PageRequest.of(0, maximum));
    }
}
