package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewReader;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CrewReaderImpl implements CrewReader {
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;

    @Override
    public Optional<Crew> findById(Long crewId) {
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
    public Page<Crew> getCrewsByUserId(Long userId, Pageable pageable) {
        List<CrewMember> crewMembers = crewMemberRepository.findByCrewMemberPK_UserId(userId);

        List<Long> crewIds = crewMembers.stream()
                .map(CrewMember::getCrewMemberPK)
                .map(CrewMemberPK::getCrewId)
                .toList();

        return crewRepository.findAllByIdIn(crewIds, pageable);
    }
}
