package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
}
