package org.orury.client.crew.application.policy;

import lombok.RequiredArgsConstructor;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.AgeUtils;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public abstract class CrewPolicy {
    private final CrewMemberReader crewMemberReader;

    public void validateCrewCreator(Long crewCreatorId, Long userId) {
        if (!Objects.equals(crewCreatorId, userId))
            throw new BusinessException(CrewErrorCode.FORBIDDEN);
    }

    public void validateCrewMember(Long crewId, Long userId) {
        if (!crewMemberReader.existsByCrewIdAndUserId(crewId, userId))
            throw new BusinessException(CrewErrorCode.NOT_CREW_MEMBER);
    }

    protected boolean meetAgeCriteria(LocalDate userBirthday, CrewDto crewDto) {
        int age = AgeUtils.calculateAge(userBirthday);
        return AgeUtils.isInRange(age, crewDto.minAge(), crewDto.maxAge());
    }

    protected boolean meetGenderCriteria(int userGender, CrewDto crewDto) {
        return crewDto.gender().getUserCode() == userGender || crewDto.gender() == CrewGender.ANY;
    }
}
