package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.CrewMember;

import java.util.List;

public interface CrewMemberReader {
    boolean existByCrewIdAndUserId(Long crewId, Long userId);

    int countByUserId(Long userId);

    List<CrewMember> getOtherCrewMembersByCrewIdMaximum(Long crewId, Long crewCreatorId, int maximum);
}
