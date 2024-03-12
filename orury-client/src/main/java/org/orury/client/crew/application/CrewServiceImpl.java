package org.orury.client.crew.application;

import lombok.RequiredArgsConstructor;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.*;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public void updateCrewImage(CrewDto crewDto, MultipartFile imageFile, Long userId) {
        validateCrewCreator(crewDto.userDto().id(), userId);
        var oldImage = crewDto.icon();
        var newImage = imageStore.upload(CREW, imageFile);
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
