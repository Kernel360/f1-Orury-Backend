package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CrewReader {
    Optional<Crew> findById(Long crewId);

    Page<Crew> getCrewsByRank(Pageable pageable);

    Page<Crew> getCrewsByRecommend(Pageable pageable);

    Page<Crew> getCrewsByUserId(Long userId, Pageable pageable);

    boolean existCrewMember(CrewMemberPK crewMemberPK);
}
