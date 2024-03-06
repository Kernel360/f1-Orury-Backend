package org.orury.client.crew.application;

import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.crew.domain.CrewReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.image.ImageReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrewServiceImpl implements CrewService {
    private final CrewReader crewReader;
    private final ImageReader imageReader;

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByRank(Pageable pageable) {
        return crewReader.getCrewsByRank(pageable)
                .map(this::transferCrewDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByRecommend(Pageable pageable) {
        return crewReader.getCrewsByRecommend(pageable)
                .map(this::transferCrewDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByUserId(Long userId, Pageable pageable) {
        return crewReader.getCrewsByUserId(userId, pageable)
                .map(this::transferCrewDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CrewDto getCrewDtoByCrewId(Long crewId) {
        Crew crew = crewReader.findCrewById(crewId)
                .orElseThrow(() -> new BusinessException(CrewErrorCode.NOT_FOUND));

        return transferCrewDto(crew);
    }

    @Override
    public boolean existCrewMember(CrewMemberPK crewMemberPK) {
        return crewReader.existCrewMember(crewMemberPK);
    }

    private CrewDto transferCrewDto(Crew crew) {
        // 크루장 프사 url
        var crewHeadUrl = imageReader.getUserImageLink(crew.getUser().getProfileImage());

        // 크루 아이콘 url
        var crewIcon = imageReader.getImageLink(S3Folder.CREW, crew.getIcon());

        // 크루원 3명까지 프사 url 이후에 추가 해야함.

        return CrewDto.from(crew, crewHeadUrl, crewIcon);

    }

}
