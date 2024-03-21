package org.orury.client.crew.application.policy;

import org.orury.domain.crew.domain.CrewApplicationReader;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.springframework.stereotype.Component;

@Component
public class CrewCreatePolicy extends CrewPolicy {
    public CrewCreatePolicy(CrewMemberReader crewMemberReader, CrewApplicationReader crewApplicationReader) {
        super(crewMemberReader, crewApplicationReader);
    }

    public void validate(CrewDto crewDto) {
        validateCrewParticipationCount(crewDto.userDto().id());
    }
}
