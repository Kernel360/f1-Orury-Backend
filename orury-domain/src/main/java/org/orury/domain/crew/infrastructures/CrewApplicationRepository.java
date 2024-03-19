package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewApplicationRepository extends JpaRepository<CrewApplication, CrewApplicationPK> {
    boolean existsByCrewApplicationPK_CrewIdAndCrewApplicationPK_UserId(Long crewId, Long userId);

    List<CrewApplication> findByCrewApplicationPK_CrewId(Long crewId);

    int countByCrewApplicationPK_UserId(Long userId);
}
