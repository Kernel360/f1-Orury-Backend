package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.Crew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    Page<Crew> findByOrderByMemberCountDesc(Pageable pageable);

    Page<Crew> findByOrderByCreatedAtDesc(Pageable pageable);

    Page<Crew> findAllByIdIn(List<Long> crewIds, Pageable pageable);
}






