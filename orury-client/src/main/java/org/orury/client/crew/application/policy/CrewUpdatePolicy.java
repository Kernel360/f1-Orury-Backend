package org.orury.client.crew.application.policy;

import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewApplicationReader;
import org.orury.domain.crew.domain.CrewApplicationStore;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class CrewUpdatePolicy extends CrewPolicy {
    private final CrewMemberReader crewMemberReader;
    private final UserReader userReader;
    private final CrewApplicationStore crewApplicationStore;

    public CrewUpdatePolicy(CrewMemberReader crewMemberReader, CrewApplicationReader crewApplicationReader, UserReader userReader, CrewApplicationStore crewApplicationStore) {
        super(crewMemberReader, crewApplicationReader);
        this.crewMemberReader = crewMemberReader;
        this.userReader = userReader;
        this.crewApplicationStore = crewApplicationStore;
    }

    public void validateUpdateCrewInfo(CrewDto oldCrew, CrewDto newCrew, Long userId) {
        validateCrewCreator(oldCrew.userDto().id(), userId);
        validateCapacity(oldCrew, newCrew);
        validateAgeOfMembers(oldCrew, newCrew);
        validateGenderOfMembers(oldCrew, newCrew);
        validatePermissionRequired(oldCrew, newCrew);
    }

    private void validateCapacity(CrewDto oldCrew, CrewDto newCrew) {
        if (newCrew.capacity() < oldCrew.memberCount())
            throw new BusinessException(CrewErrorCode.MEMBER_OVERFLOW);
    }

    private void validateAgeOfMembers(CrewDto oldCrew, CrewDto newCrew) {
        if (newCrew.minAge() <= oldCrew.minAge() && oldCrew.maxAge() <= newCrew.maxAge())
            return;

        List<User> members = crewMemberReader.getMembersByCrewId(oldCrew.id());
        boolean outOfAgeRange = members.stream()
                .map(User::getBirthday)
                .anyMatch(birthday -> !meetAgeCriteria(birthday, newCrew));
        if (outOfAgeRange) throw new BusinessException(CrewErrorCode.AGE_FORBIDDEN);
    }

    private void validateGenderOfMembers(CrewDto oldCrew, CrewDto newCrew) {
        if (oldCrew.gender() == newCrew.gender() || newCrew.gender() == CrewGender.ANY)
            return;

        List<User> members = crewMemberReader.getMembersByCrewId(oldCrew.id());
        boolean differentGender = members.stream()
                .map(User::getGender)
                .anyMatch(gender -> !meetGenderCriteria(gender, newCrew));
        if (differentGender) throw new BusinessException(CrewErrorCode.GENDER_FORBIDDEN);
    }

    private void validatePermissionRequired(CrewDto oldCrew, CrewDto newCrew) {
        // 크루 PermissionRequired가 변경되지 않는 경우 || PermitionRequired false -> true 로 변경되는 경우
        if (oldCrew.permissionRequired() == newCrew.permissionRequired() || newCrew.permissionRequired())
            return;

        // 현재 크루에 지원정보 모두 가져옴.
        List<CrewApplication> applications = crewApplicationReader.findAllByCrewId(oldCrew.id());

        // 유효한 지원정보 필터링
        List<CrewApplication> validApplications = applications.stream()
                .filter(crewApplication -> {
                    User user = userReader.getUserById(crewApplication.getCrewApplicationPK().getUserId());
                    return meetAgeCriteria(user.getBirthday(), newCrew)
                            && meetGenderCriteria(user.getGender(), newCrew);
                }).toList();

        // 변경하고자하는 정원보다 '크루멤버 수 + 유효한 지원자 수'가 더 큰 경우
        if (newCrew.capacity() < oldCrew.memberCount() + validApplications.size())
            throw new BusinessException(CrewErrorCode.APPLICATION_OVERFLOW);

        // 유효한 지원자들에 대해 승인
        validApplications.stream()
                .map(CrewApplication::getCrewApplicationPK)
                .forEach(crewApplicationStore::approve);

        applications.stream()
                .filter(crewApplication -> !validApplications.contains(crewApplication))
                .map(CrewApplication::getCrewApplicationPK)
                .forEach(crewApplicationStore::delete);
    }
}
