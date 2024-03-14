package org.orury.domain.crew.domain;

import org.orury.domain.user.domain.entity.User;

import java.util.List;

public interface CrewMemberReader {
    boolean existByCrewIdAndUserId(Long crewId, Long userId);

    int countByUserId(Long userId);

    List<User> getMembersByCrewId(Long crewId);
}
