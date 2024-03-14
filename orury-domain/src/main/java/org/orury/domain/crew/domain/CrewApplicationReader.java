package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.CrewApplication;

import java.util.List;

public interface CrewApplicationReader {
    boolean existsByCrewIdAndUserId(Long crewId, Long userId);

    List<CrewApplication> findAllByCrewId(Long crewId);

    int countByUserId(Long userId);
}
