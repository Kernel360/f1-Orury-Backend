package org.orury.client.crew.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.crew.interfaces.request.CrewCreateRequest;
import org.orury.client.crew.interfaces.response.CrewResponse;
import org.orury.client.crew.interfaces.response.CrewsResponse;
import org.orury.client.user.application.UserService;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.orury.domain.global.constants.NumberConstants.CREW_PAGINATION_SIZE;

@Component
@RequiredArgsConstructor
public class CrewFacade {
    private final CrewService crewService;
    private final UserService userService;

    public void createCrew(CrewCreateRequest request, MultipartFile image, Long userId) {
        var userDto = userService.getUserDtoById(userId);
        var crewDto = request.toDto(userDto);
        crewService.createCrew(crewDto, image);
    }

    public Page<CrewsResponse> getCrewsByRank(int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByRank(pageRequest);
        return convertCrewDtosToCrewsResponses(crewDtos);
    }

    public Page<CrewsResponse> getCrewsByRecommend(int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByRecommend(pageRequest);
        return convertCrewDtosToCrewsResponses(crewDtos);
    }

    public Page<CrewsResponse> getMyCrews(Long userId, int page) {
        var pageRequest = PageRequest.of(page, CREW_PAGINATION_SIZE);
        Page<CrewDto> crewDtos = crewService.getCrewDtosByUserId(userId, pageRequest);
        return convertCrewDtosToCrewsResponses(crewDtos);
    }

    public CrewResponse getCrewByCrewId(Long userId, Long crewId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        CrewMemberPK crewMemberPK = CrewMemberPK.of(userId, crewId);

        boolean isApply = crewService.existCrewMember(crewMemberPK);

        return CrewResponse.of(crewDto, isApply);
    }

    public void updateCrewImage(Long crewId, MultipartFile image, Long userId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.updateCrewImage(crewDto, image, userId);
    }

    private Page<CrewsResponse> convertCrewDtosToCrewsResponses(Page<CrewDto> crewDtos) {
        return crewDtos.map(crewDto -> {
            List<String> userImages = crewService.getUserImagesByCrew(crewDto);
            return CrewsResponse.of(crewDto, userImages);
        });
    }
}
