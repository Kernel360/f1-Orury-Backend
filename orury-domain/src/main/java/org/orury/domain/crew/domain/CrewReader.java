package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrewReader {
    Optional<Crew> findCrewById(Long crewId);

    Page<Crew> getCrewsByRank(Pageable pageable);

    Page<Crew> getCrewsByRecommend(Pageable pageable);

    Page<Crew> getCrewsByCrewId(List<Long> crewId, Pageable pageable);

    List<CrewMember> getCrewMembersByUserId(Long userId);

    boolean existCrewMember(CrewMemberPK crewMemberPK);
}
