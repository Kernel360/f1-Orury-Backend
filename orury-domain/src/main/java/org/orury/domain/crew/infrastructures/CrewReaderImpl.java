package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.CrewReader;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CrewReaderImpl implements CrewReader {
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;

    @Override
    public Optional<Crew> findCrewById(Long crewId) {
        return crewRepository.findById(crewId);
    }

    @Override
    public Page<Crew> getCrewsByRank(Pageable pageable) {
        return crewRepository.findByOrderByMemberCountDesc(pageable);
    }

    @Override
    public Page<Crew> getCrewsByRecommend(Pageable pageable) {
        return crewRepository.findByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<Crew> getCrewsByCrewId(List<Long> crewIds, Pageable pageable) {
        return crewRepository.findAllByIdIn(crewIds, pageable);
    }

    @Override
    public List<CrewMember> getCrewMembersByUserId(Long userId) {
        //  유저ID와 일치하는 CrewMember 가져오기
        return crewMemberRepository.findByCrewMemberPK_UserId(userId);
    }

    @Override
    public boolean existCrewMember(CrewMemberPK crewMemberPK) {
        return crewMemberRepository.existsById(crewMemberPK);
    }
}
