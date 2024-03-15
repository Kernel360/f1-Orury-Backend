package org.orury.client.crew.application;

import static org.orury.common.util.S3Folder.CREW;

import org.orury.client.global.image.ImageAsyncStore;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.CrewMemberStore;
import org.orury.domain.crew.domain.CrewReader;
import org.orury.domain.crew.domain.CrewStore;
import org.orury.domain.crew.domain.CrewTagReader;
import org.orury.domain.crew.domain.CrewTagStore;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageStore;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {
    private final CrewReader crewReader;
    private final CrewStore crewStore;
    private final CrewTagReader crewTagReader;
    private final CrewTagStore crewTagStore;
    private final CrewMemberReader crewMemberReader;
    private final CrewMemberStore crewMemberStore;
    private final UserReader userReader;
    private final ImageAsyncStore imageasyncStore;
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
        var icon = imageasyncStore.upload(CREW, file);
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
        return crewReader.existCrewMember(crewMemberPK);
    }

    @Override
    @Transactional
    public void updateCrewImage(CrewDto crewDto, MultipartFile imageFile, Long userId) {
        validateCrewCreator(crewDto.userDto().id(), userId);
        var oldImage = crewDto.icon();
        var newImage = imageasyncStore.upload(CREW, imageFile);
        crewStore.save(crewDto.toEntity(newImage));
        imageStore.delete(CREW, oldImage);
    }

    private void validateCrewCreator(Long crewCreatorId, Long userId) {
        if (!Objects.equals(crewCreatorId, userId))
            throw new BusinessException(CrewErrorCode.FORBIDDEN);
    }

    private void validateCrewParticipationCount(Long userId) {
        int participationCount = crewMemberReader.countByUserId(userId);
        if (NumberConstants.MAXIMUM_CREW_PARTICIPATION <= participationCount)
            throw new BusinessException(CrewErrorCode.MAXIMUM_PARTICIPATION);
    }
}
