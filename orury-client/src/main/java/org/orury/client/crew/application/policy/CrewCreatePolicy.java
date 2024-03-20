package org.orury.client.crew.application.policy;

import lombok.RequiredArgsConstructor;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewApplicationReader;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.global.constants.NumberConstants;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CrewCreatePolicy {
    private final CrewMemberReader crewMemberReader;
    private final CrewApplicationReader crewApplicationReader;

    public void validate(CrewDto crewDto) {
        validateCrewParticipationCount(crewDto.userDto().id());
    }

    private void validateCrewParticipationCount(Long userId) {
        int participatingCrewCount = crewMemberReader.countByUserId(userId);
        int applyingCrewCount = crewApplicationReader.countByUserId(userId);
        if (NumberConstants.MAXIMUM_CREW_PARTICIPATION <= participatingCrewCount + applyingCrewCount)
            throw new BusinessException(CrewErrorCode.MAXIMUM_PARTICIPATION);
    }
}
