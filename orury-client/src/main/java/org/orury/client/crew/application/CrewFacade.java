package org.orury.client.crew.application;

import static org.orury.domain.global.constants.NumberConstants.CREW_PAGINATION_SIZE;

import org.orury.client.crew.interfaces.response.CrewResponse;
import org.orury.client.crew.interfaces.response.CrewsResponseByMyCrew;
import org.orury.client.crew.interfaces.response.CrewsResponseByRank;
import org.orury.client.crew.interfaces.response.CrewsResponseByRecommend;
import org.orury.client.global.WithPageResponse;
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

    public WithPageResponse<CrewsResponseByRank> getCrewsByRank(int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByRank(pageRequest);
        int nextPage = crewService.getNextPage(crewDtos, page);

        return WithPageResponse.of(
                crewDtos.stream()
                        .map(CrewsResponseByRank::of)
                        .toList(), nextPage
        );
    }

    public WithPageResponse<CrewsResponseByRecommend> getCrewsByRecommend(int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByRecommend(pageRequest);
        int nextPage = crewService.getNextPage(crewDtos, page);

        return WithPageResponse.of(
                crewDtos.stream()
                        .map(CrewsResponseByRecommend::of)
                        .toList(), nextPage
        );
    }

    public WithPageResponse<CrewsResponseByMyCrew> getMyCrews(Long userId, int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByUserId(userId, pageRequest);
        int nextPage = crewService.getNextPage(crewDtos, page);

        return WithPageResponse.of(
                crewDtos.stream()
                        .map(CrewsResponseByMyCrew::of)
                        .toList(), nextPage
        );
    }

    public CrewResponse getCrewByCrewId(Long userId, Long crewId) {
        CrewDto crewDto = crewService.getCrewDtoByCrewId(crewId);
        CrewMemberPK crewMemberPK = CrewMemberPK.of(userId, crewId);

        boolean isApply = crewService.existCrewMember(crewMemberPK);

        return CrewResponse.of(crewDto, isApply);
    }
}
