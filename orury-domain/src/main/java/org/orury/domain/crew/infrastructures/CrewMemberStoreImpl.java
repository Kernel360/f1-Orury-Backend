package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMemberStore;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMemberStoreImpl implements CrewMemberStore {
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRepository crewRepository;

    @Override
    public void addCrewMember(Long crewId, Long userId) {
        var crewMember = CrewMember.of(CrewMemberPK.of(userId, crewId), null, null);
        crewMemberRepository.save(crewMember);
        crewRepository.increaseMemberCount(crewId);
    }

    @Override
    public void subtractCrewMember(Long crewId, Long userId) {
        var crewMemberPK = CrewMemberPK.of(userId, crewId);
        crewMemberRepository.deleteById(crewMemberPK);
        crewRepository.decreaseMemberCount(crewId);
    }
}
