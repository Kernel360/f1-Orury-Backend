package org.orury.client.crew.application;

import org.orury.domain.crew.domain.dto.CrewDto;

public interface CrewService {
    CrewDto getCrewDtoById(Long crewId);
}
