package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.user.domain.entity.User;

import java.util.List;

public interface CrewMemberReader {
    boolean existsByCrewIdAndUserId(Long crewId, Long userId);

    int countByUserId(Long userId);

    List<User> getMembersByCrewId(Long crewId);

    List<CrewMember> getOtherCrewMembersByCrewIdMaximum(Long crewId, Long crewCreatorId, int maximum);
}