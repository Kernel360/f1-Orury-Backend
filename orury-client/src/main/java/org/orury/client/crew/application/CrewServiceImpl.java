package org.orury.client.crew.application;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.orury.client.global.image.ImageAsyncStore;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.AgeUtils;
import org.orury.domain.crew.domain.*;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.crew.domain.entity.CrewMember;
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
import java.util.LinkedList;
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
    private final ImageAsyncStore imageAsyncStore;


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
        var icon = imageAsyncStore.upload(CREW, file);
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
    public List<String> getUserImagesByCrew(CrewDto crewDto) {
        UserDto crewCreator = crewDto.userDto();
        List<CrewMember> otherMembers = crewMemberReader.getOtherCrewMembersByCrewIdMaximum(crewDto.id(), crewCreator.id(), NumberConstants.MAXIMUM_OF_CREW_THUMBNAILS - 1);

        List<String> userImages = new LinkedList<>();
        userImages.add(crewCreator.profileImage());
        otherMembers.forEach(crewMember -> {
            User user = userReader.getUserById(crewMember.getCrewMemberPK().getUserId());
            userImages.add(user.getProfileImage());
        });
        return userImages;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existCrewMember(CrewMemberPK crewMemberPK) {
        return crewMemberReader.existsByCrewMemberPK(crewMemberPK);
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
        var newImage = imageAsyncStore.upload(CREW, imageFile);
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
        // 이미 크루원인 경우
        if (crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
            throw new BusinessException(CrewErrorCode.ALREADY_MEMBER);

        // 크루 참여 검증 (멤버로 참여한 크루 + 신청한 크루 <= 5)
        validateCrewParticipationCount(crewDto.userDto().id());

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

        // 크루 가입신청 저장
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
        if (!crewMemberReader.existsByCrewIdAndUserId(crewId, userId))
            throw new BusinessException(CrewErrorCode.NOT_CREW_MEMBER);
    }

    private void validateApplication(Long crewId, Long applicantId) {
        if (!crewApplicationReader.existsByCrewIdAndUserId(crewId, applicantId))
            throw new BusinessException(CrewErrorCode.NOT_FOUND_APPLICATION);
    }

    private void validateCrewParticipationCount(Long userId) {
        int participatingCrewCount = crewMemberReader.countByUserId(userId);
        int applyingCrewCount = crewApplicationReader.countByUserId(userId);
        if (NumberConstants.MAXIMUM_CREW_PARTICIPATION <= participatingCrewCount + applyingCrewCount)
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

        // 유효하지 않은 지원자들에 대해 거절
        applications.removeAll(validApplications);
        applications.stream()
                .map(CrewApplication::getCrewApplicationPK)
                .forEach(crewApplicationStore::delete);
    }
}