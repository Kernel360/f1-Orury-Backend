package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;
import org.orury.domain.user.domain.dto.UserDto;

public interface CrewApplicationStore {
    void save(CrewDto crewDto, UserDto userDto, String answer);

    void approve(Long crewId, Long userId);

    void approve(CrewApplicationPK crewApplicationPK);

    void delete(Long crewId, Long userId);

    void delete(CrewApplicationPK crewApplicationPK);
}
