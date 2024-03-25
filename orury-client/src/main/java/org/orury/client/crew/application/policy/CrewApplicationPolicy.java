package org.orury.client.crew.application.policy;

import org.apache.logging.log4j.util.Strings;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewApplicationReader;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.CrewMemberStore;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class CrewApplicationPolicy extends CrewPolicy {
    private final CrewMemberReader crewMemberReader;
    private final CrewMemberStore crewMemberStore;

    public CrewApplicationPolicy(CrewApplicationReader crewApplicationReader, CrewMemberReader crewMemberReader, CrewMemberStore crewMemberStore) {
        super(crewMemberReader, crewApplicationReader);
        this.crewMemberReader = crewMemberReader;
        this.crewMemberStore = crewMemberStore;
    }

    public void validateApplyCrew(CrewDto crewDto, UserDto userDto, String answer) {
        // 이미 크루원인 경우
        if (crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
            throw new BusinessException(CrewErrorCode.ALREADY_MEMBER);

        // 크루 참여 검증 (멤버로 참여한 크루 + 신청한 크루 <= 5)
        validateCrewParticipationCount(userDto.id());

        // 지원하는 크루 연령기준에 만족하지 않는 경우
        if (!meetAgeCriteria(userDto.birthday(), crewDto))
            throw new BusinessException(CrewErrorCode.AGE_FORBIDDEN);
        // 지원하는 크루 성별기준에 만족하지 않는 경우
        if (!meetGenderCriteria(userDto.gender(), crewDto))
            throw new BusinessException(CrewErrorCode.GENDER_FORBIDDEN);

        // 지원하는 크루가 즉시 가입인 경우
        if (!crewDto.permissionRequired()) {
            crewMemberStore.addCrewMember(crewDto.id(), userDto.id());
            return;
        }

        // 지원하는 크루가 답변이 필수인 경우 && 제출한 답변이 비어있는 경우
        if (crewDto.answerRequired() && Strings.isBlank(answer))
            throw new BusinessException(CrewErrorCode.EMPTY_ANSWER);
    }

    public void validateApplication(Long crewId, Long applicantId) {
        if (!crewApplicationReader.existsByCrewIdAndUserId(crewId, applicantId))
            throw new BusinessException(CrewErrorCode.NOT_FOUND_APPLICATION);
    }
}
