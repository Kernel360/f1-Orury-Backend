package org.orury.client.crew.application;

import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.crew.domain.CrewReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrewServiceImpl implements CrewService {
    private final CrewReader crewReader;
    private final ImageReader imageReader;

    @Override
    public Page<CrewDto> getCrewDtosByRank(Pageable pageable) {
        return crewReader.getCrewsByRank(pageable)
                .map(this::transferCrewDto);
    }

    @Override
    public Page<CrewDto> getCrewDtosByRecommend(Pageable pageable) {
        return crewReader.getCrewsByRecommend(pageable)
                .map(this::transferCrewDto);
    }

    @Override
    public Page<CrewDto> getCrewDtosByUserId(Long userId, Pageable pageable) {
        List<CrewMember> crewMembers = crewReader.getCrewMembersByUserId(userId);

        List<Long> crewIds = crewMembers.stream()
                .map(CrewMember::getCrewMemberPK)
                .map(CrewMemberPK::getCrewId)
                .toList();

        return crewReader.getCrewsByCrewId(crewIds, pageable)
                .map(this::transferCrewDto);
    }

    @Override
    public CrewDto getCrewDtoByCrewId(Long crewId) {
        Crew crew = crewReader.findCrewById(crewId)
                .orElseThrow(() -> new BusinessException(CrewErrorCode.NOT_FOUND));

        return transferCrewDto(crew);
    }

    @Override
    public int getNextPage(Page<CrewDto> crewDtos, int page) {
        return (crewDtos.hasNext()) ? page + 1 : NumberConstants.LAST_PAGE;
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
