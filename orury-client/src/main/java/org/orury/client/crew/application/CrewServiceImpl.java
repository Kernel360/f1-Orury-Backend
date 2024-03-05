package org.orury.client.crew.application;

import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
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
                .map(CrewDto::from);
    }

    @Override
    public Page<CrewDto> getCrewDtosByRecommend(Pageable pageable) {
        return crewReader.getCrewsByRecommend(pageable)
                .map(CrewDto::from);
    }

    @Override
    public Page<CrewDto> getCrewDtosByUserId(Long userId, Pageable pageable) {
        List<CrewMember> crewMembers = crewReader.getCrewMembersByUserId(userId);

        List<Long> crewIds = crewMembers.stream()
                .map(CrewMember::getCrewMemberPK)
                .map(CrewMemberPK::getCrewId)
                .toList();

        return crewReader.getCrewsByCrewId(crewIds, pageable)
                .map(CrewDto::from);
    }

    @Override
    public CrewDto getCrewDtoByCrewId(Long crewId) {
        Crew crew = crewReader.findCrewById(crewId)
                .orElseThrow(() -> new BusinessException(CrewErrorCode.NOT_FOUND));

        return CrewDto.from(crew);
    }

    @Override
    public int getNextPage(Page<CrewDto> crewDtos, int page) {
        return (crewDtos.hasNext()) ? page + 1 : NumberConstants.LAST_PAGE;
    }

    @Override
    public boolean existCrewMember(CrewMemberPK crewMemberPK) {
        return crewReader.existCrewMember(crewMemberPK);
    }


}
