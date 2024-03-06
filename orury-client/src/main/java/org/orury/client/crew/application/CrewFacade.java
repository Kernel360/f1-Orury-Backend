package org.orury.client.crew.application;

import static org.orury.domain.global.constants.NumberConstants.CREW_PAGINATION_SIZE;

import org.orury.client.crew.interfaces.response.CrewResponse;
import org.orury.client.crew.interfaces.response.CrewsResponseByMyCrew;
import org.orury.client.crew.interfaces.response.CrewsResponseByRank;
import org.orury.client.crew.interfaces.response.CrewsResponseByRecommend;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CrewFacade {
    private final CrewService crewService;

    public Page<CrewsResponseByRank> getCrewsByRank(int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByRank(pageRequest);
        return crewDtos.map(CrewsResponseByRank::of);
    }

    public Page<CrewsResponseByRecommend> getCrewsByRecommend(int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByRecommend(pageRequest);
        return crewDtos.map(CrewsResponseByRecommend::of);
    }

    public Page<CrewsResponseByMyCrew> getMyCrews(Long userId, int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByUserId(userId, pageRequest);
        return crewDtos.map(CrewsResponseByMyCrew::of);
    }

    public CrewResponse getCrewByCrewId(Long userId, Long crewId) {
        CrewDto crewDto = crewService.getCrewDtoByCrewId(crewId);
        CrewMemberPK crewMemberPK = CrewMemberPK.of(userId, crewId);

        boolean isApply = crewService.existCrewMember(crewMemberPK);

        return CrewResponse.of(crewDto, isApply);
    }
}
