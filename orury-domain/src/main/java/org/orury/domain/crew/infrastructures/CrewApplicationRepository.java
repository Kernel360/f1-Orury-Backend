package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewApplicationRepository extends JpaRepository<CrewApplication, CrewApplicationPK> {
}
