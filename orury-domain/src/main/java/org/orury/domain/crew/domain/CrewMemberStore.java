package org.orury.domain.crew.domain;

public interface CrewMemberStore {
    void addCrewMember(Long crewId, Long userId);

    void subtractCrewMember(Long crewId, Long userId);
}
