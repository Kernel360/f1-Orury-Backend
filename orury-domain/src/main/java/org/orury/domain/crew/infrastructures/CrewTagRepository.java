package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.CrewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewTagRepository extends JpaRepository<CrewTag, Long> {
    List<CrewTag> findCrewTagByCrewId(Long crewId);

    void deleteAllByCrewId(Long crewId);
}
