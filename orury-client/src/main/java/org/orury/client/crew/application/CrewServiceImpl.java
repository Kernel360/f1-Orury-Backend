package org.orury.client.crew.application;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.AgeUtils;
import org.orury.domain.crew.domain.*;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.orury.domain.meeting.domain.MeetingStore;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.orury.common.util.S3Folder.CREW;

@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {
    private final CrewReader crewReader;
    private final CrewStore crewStore;
    private final CrewTagReader crewTagReader;
    private final CrewTagStore crewTagStore;
    private final CrewMemberReader crewMemberReader;
    private final CrewMemberStore crewMemberStore;
    private final CrewApplicationReader crewApplicationReader;
    private final CrewApplicationStore crewApplicationStore;
    private final MeetingStore meetingStore;
    private final MeetingMemberStore meetingMemberStore;
    private final UserReader userReader;
    private final ImageStore imageStore;


    @Override
    @Transactional(readOnly = true)
    public CrewDto getCrewDtoById(Long crewId) {
        Crew crew = crewReader.findById(crewId)
                .orElseThrow(() -> new BusinessException(CrewErrorCode.NOT_FOUND));
        List<String> tags = crewTagReader.getTagsByCrewId(crewId);
        return CrewDto.from(crew, tags);
    }

    @Override
    @Transactional
    public void createCrew(CrewDto crewDto, MultipartFile file) {
        validateCrewParticipationCount(crewDto.userDto().id());
        var icon = imageStore.upload(CREW, file);
        Crew crew = crewStore.save(crewDto.toEntity(icon));
        crewTagStore.addTags(crew, crewDto.tags());
        crewMemberStore.addCrewMember(crew.getId(), crew.getUser().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByRank(Pageable pageable) {
        return crewReader.getCrewsByRank(pageable)
                .map(CrewDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByRecommend(Pageable pageable) {
        return crewReader.getCrewsByRecommend(pageable)
                .map(CrewDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByUserId(Long userId, Pageable pageable) {
        return crewReader.getCrewsByUserId(userId, pageable)
                .map(CrewDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existCrewMember(CrewMemberPK crewMemberPK) {
        return crewReader.existCrewMember(crewMemberPK);
    }

    @Override
    @Transactional
    public void updateCrewInfo(CrewDto oldCrew, CrewDto newCrew, Long userId) {
        validateCrewCreator(oldCrew.userDto().id(), userId);
        if (newCrew.capacity() < oldCrew.memberCount())
            throw new BusinessException(CrewErrorCode.MEMBER_OVERFLOW);
        validateAgeOfMembers(oldCrew, newCrew); // TODO: 최고연령 변경 안 해도, 새해가 됨에 따라 연령 걸리는 멤버도 존재할듯..?
        validateGenderOfMembers(oldCrew, newCrew);
        validatePermissionRequired(oldCrew, newCrew);

        Crew crew = crewStore.save(newCrew.toEntity());
        crewTagStore.updateTags(oldCrew.tags(), newCrew.tags(), crew);
    }

    @Override
    @Transactional
    public void updateCrewImage(CrewDto crewDto, MultipartFile imageFile, Long userId) {
        validateCrewCreator(crewDto.userDto().id(), userId);
        var oldImage = crewDto.icon();
        var newImage = imageStore.upload(CREW, imageFile);
        crewStore.save(crewDto.toEntity(newImage));
        imageStore.delete(CREW, oldImage);
    }

    @Override
    @Transactional
    public void deleteCrew(CrewDto crewDto, Long userId) {
        validateCrewCreator(crewDto.userDto().id(), userId);
        crewStore.delete(crewDto.toEntity());
        imageStore.delete(CREW, crewDto.icon());
    }

    @Override
    @Transactional
    public void applyCrew(CrewDto crewDto, UserDto userDto, String answer) {
        if (crewMemberReader.existByCrewIdAndUserId(crewDto.id(), userDto.id()))
            throw new BusinessException(CrewErrorCode.ALREADY_MEMBER);

        validateCrewParticipationCount(crewDto.userDto().id());

        if (!meetAgeCriteria(userDto.birthday(), crewDto))
            throw new BusinessException(CrewErrorCode.AGE_FORBIDDEN);
        if (!meetGenderCriteria(userDto.gender(), crewDto))
            throw new BusinessException(CrewErrorCode.GENDER_FORBIDDEN);

        if (!crewDto.permissionRequired())
            crewMemberStore.addCrewMember(crewDto.id(), userDto.id());

        if (crewDto.answerRequired() && Strings.isBlank(answer))
            throw new BusinessException(CrewErrorCode.EMPTY_ANSWER);

        crewApplicationStore.save(crewDto, userDto, answer);
    }

    @Override
    @Transactional
    public void withdrawApplication(CrewDto crewDto, Long userId) {
        validateApplication(crewDto.id(), userId);
        crewApplicationStore.delete(crewDto.id(), userId);
    }

    @Override
    @Transactional
    public void approveApplication(CrewDto crewDto, Long applicantId, Long userId) {
        validateCrewCreator(crewDto.userDto().id(), userId);
        validateApplication(crewDto.id(), applicantId);
        crewApplicationStore.approve(crewDto.id(), applicantId);
    }

    @Override
    @Transactional
    public void disapproveApplication(CrewDto crewDto, Long applicantId, Long userId) {
        validateCrewCreator(crewDto.userDto().id(), userId);
        validateApplication(crewDto.id(), applicantId);
        crewApplicationStore.delete(crewDto.id(), applicantId);
    }

    @Override
    @Transactional
    public void leaveCrew(CrewDto crewDto, Long userId) {
        if (Objects.equals(crewDto.userDto().id(), userId))
            throw new BusinessException(CrewErrorCode.CREATOR_DELETE_FORBIDDEN);
        removeMember(crewDto.id(), userId);
    }

    @Override
    @Transactional
    public void expelMember(CrewDto crewDto, Long memberId, Long userId) {
        validateCrewCreator(crewDto.userDto().id(), userId);
        if (Objects.equals(crewDto.userDto().id(), memberId))
            throw new BusinessException(CrewErrorCode.CREATOR_DELETE_FORBIDDEN);
        removeMember(crewDto.id(), memberId);
    }

    private void removeMember(Long crewId, Long memberId) {
        validateCrewMember(crewId, memberId);

        meetingMemberStore.removeAllByUserIdAndCrewId(memberId, crewId);
        meetingStore.deleteAllByUserIdAndCrewId(memberId, crewId);

        crewMemberStore.subtractCrewMember(crewId, memberId);
    }

    private boolean meetAgeCriteria(LocalDate userBirthday, CrewDto crewDto) {
        int age = AgeUtils.calculateAge(userBirthday);
        return AgeUtils.isInRange(age, crewDto.minAge(), crewDto.maxAge());
    }

    private boolean meetGenderCriteria(int userGender, CrewDto crewDto) {
        return crewDto.gender().getUserCode() == userGender || crewDto.gender() == CrewGender.ANY;
    }

    private void validateCrewCreator(Long crewCreatorId, Long userId) {
        if (!Objects.equals(crewCreatorId, userId))
            throw new BusinessException(CrewErrorCode.FORBIDDEN);
    }

    private void validateCrewMember(Long crewId, Long userId) {
        if (!crewMemberReader.existByCrewIdAndUserId(crewId, userId))
            throw new BusinessException(CrewErrorCode.NOT_CREW_MEMBER);
    }

    private void validateApplication(Long crewId, Long applicantId) {
        if (!crewApplicationReader.existsByCrewIdAndUserId(crewId, applicantId))
            throw new BusinessException(CrewErrorCode.NOT_FOUND_APPLICATION);
    }

    private void validateCrewParticipationCount(Long userId) {
        int memberCount = crewMemberReader.countByUserId(userId);
        int applicationCount = crewApplicationReader.countByUserId(userId);
        if (NumberConstants.MAXIMUM_CREW_PARTICIPATION <= memberCount + applicationCount)
            throw new BusinessException(CrewErrorCode.MAXIMUM_PARTICIPATION);
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
        if (oldCrew.permissionRequired() == newCrew.permissionRequired() || newCrew.permissionRequired())
            return;

        List<CrewApplication> applications = crewApplicationReader.findAllByCrewId(oldCrew.id());

        List<CrewApplication> validApplications = applications.stream()
                .filter(crewApplication -> {
                    User user = userReader.getUserById(crewApplication.getCrewApplicationPK().getUserId());
                    return meetAgeCriteria(user.getBirthday(), newCrew)
                            && meetGenderCriteria(user.getGender(), newCrew);
                }).toList();

        if (newCrew.capacity() < oldCrew.memberCount() + validApplications.size())
            throw new BusinessException(CrewErrorCode.APPLICATION_OVERFLOW);

        validApplications.stream()
                .map(CrewApplication::getCrewApplicationPK)
                .forEach(crewApplicationStore::approve);

        applications.removeAll(validApplications);
        applications.stream()
                .map(CrewApplication::getCrewApplicationPK)
                .forEach(crewApplicationStore::delete);
    }
}
