package org.orury.client.crew.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.crew.application.policy.CrewApplicationPolicy;
import org.orury.client.crew.application.policy.CrewCreatePolicy;
import org.orury.client.crew.application.policy.CrewPolicy;
import org.orury.client.crew.application.policy.CrewUpdatePolicy;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.*;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.image.domain.ImageStore;
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
    private final CrewApplicationStore crewApplicationStore;
    private final MeetingStore meetingStore;
    private final MeetingMemberStore meetingMemberStore;
    private final UserReader userReader;
    private final ImageStore imageStore;

    private final CrewPolicy crewPolicy;

    private final CrewCreatePolicy crewCreatePolicy;
    private final CrewUpdatePolicy crewUpdatePolicy;
    private final CrewApplicationPolicy crewApplicationPolicy;

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
        crewCreatePolicy.validate(crewDto);
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
        crewUpdatePolicy.validateUpdateCrewInfo(oldCrew, newCrew, userId);
        Crew crew = crewStore.save(newCrew.toEntity());
        crewTagStore.updateTags(oldCrew.tags(), newCrew.tags(), crew);
    }

    @Override
    @Transactional
    public void updateCrewImage(CrewDto crewDto, MultipartFile imageFile, Long userId) {
        crewPolicy.validateCrewCreator(crewDto.userDto().id(), userId);
        var oldImage = crewDto.icon();
        var newImage = imageStore.upload(CREW, imageFile);
        crewStore.save(crewDto.toEntity(newImage));
        imageStore.delete(CREW, oldImage);
    }

    @Override
    @Transactional
    public void deleteCrew(CrewDto crewDto, Long userId) {
        crewPolicy.validateCrewCreator(crewDto.userDto().id(), userId);
        crewStore.delete(crewDto.toEntity());
        imageStore.delete(CREW, crewDto.icon());
    }

    @Override
    @Transactional
    public void applyCrew(CrewDto crewDto, UserDto userDto, String answer) {
        crewApplicationPolicy.validateApplyCrew(crewDto, userDto, answer);

        // 지원하는 크루가 즉시 가입인 경우
        if (!crewDto.permissionRequired()) {
            crewMemberStore.addCrewMember(crewDto.id(), userDto.id());
            return;
        }

        crewApplicationStore.save(crewDto, userDto, answer);
    }

    @Override
    @Transactional
    public void withdrawApplication(CrewDto crewDto, Long userId) {
        crewApplicationPolicy.validateApplication(crewDto.id(), userId);
        crewApplicationStore.delete(crewDto.id(), userId);
    }

    @Override
    @Transactional
    public void approveApplication(CrewDto crewDto, Long applicantId, Long userId) {
        crewPolicy.validateCrewCreator(crewDto.userDto().id(), userId);
        crewApplicationPolicy.validateApplication(crewDto.id(), applicantId);
        crewApplicationStore.approve(crewDto.id(), applicantId);
    }

    @Override
    @Transactional
    public void disapproveApplication(CrewDto crewDto, Long applicantId, Long userId) {
        crewPolicy.validateCrewCreator(crewDto.userDto().id(), userId);
        crewApplicationPolicy.validateApplication(crewDto.id(), applicantId);
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
        crewPolicy.validateCrewCreator(crewDto.userDto().id(), userId);
        if (Objects.equals(crewDto.userDto().id(), memberId))
            throw new BusinessException(CrewErrorCode.CREATOR_DELETE_FORBIDDEN);
        removeMember(crewDto.id(), memberId);
    }

    private void removeMember(Long crewId, Long memberId) {
        crewPolicy.validateCrewMember(crewId, memberId);

        meetingMemberStore.removeAllByUserIdAndCrewId(memberId, crewId);
        meetingStore.deleteAllByUserIdAndCrewId(memberId, crewId);

        crewMemberStore.subtractCrewMember(crewId, memberId);
    }
}
