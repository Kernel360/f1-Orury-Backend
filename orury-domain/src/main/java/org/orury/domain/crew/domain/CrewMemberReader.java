package org.orury.domain.crew.domain;

public interface CrewMemberReader {
    boolean existByCrewIdAndUserId(Long crewId, Long userId);

    int countByUserId(Long userId);
}
