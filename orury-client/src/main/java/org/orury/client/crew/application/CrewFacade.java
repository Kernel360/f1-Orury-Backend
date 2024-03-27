package org.orury.client.crew.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.crew.interfaces.message.CrewMessage;
import org.orury.client.crew.interfaces.request.CrewRequest;
import org.orury.client.crew.interfaces.response.CrewResponse;
import org.orury.client.crew.interfaces.response.CrewsResponse;
import org.orury.client.user.application.UserService;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.user.domain.dto.UserDto;
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

    public void createCrew(CrewRequest request, MultipartFile image, Long userId) {
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
        boolean isMember = crewService.existCrewMember(crewId, userId);
        List<String> userImages = crewService.getUserImagesByCrew(crewDto);
        return CrewResponse.of(crewDto, isMember, userImages);
    }

    public void updateCrewInfo(Long crewId, CrewRequest request, Long userId) {
        var oldCrewDto = crewService.getCrewDtoById(crewId);
        var newCrewDto = request.toDto(oldCrewDto);
        crewService.updateCrewInfo(oldCrewDto, newCrewDto, userId);
    }

    public void updateCrewImage(Long crewId, MultipartFile image, Long userId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.updateCrewImage(crewDto, image, userId);
    }

    public void deleteCrew(Long crewId, Long userId) { // TODO: 삭제 유예(약 7일?)는 구현 필요.
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.deleteCrew(crewDto, userId);
    }

    public CrewMessage applyCrew(Long crewId, Long userId, String answer) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        UserDto userDto = userService.getUserDtoById(userId);
        return crewService.applyCrew(crewDto, userDto, answer);
    }

    public void withdrawApplication(Long crewId, Long userId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.withdrawApplication(crewDto, userId);
    }

    public void approveApplication(Long crewId, Long applicantId, Long userId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.approveApplication(crewDto, applicantId, userId);
    }

    public void disapproveApplication(Long crewId, Long applicantId, Long userId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.disapproveApplication(crewDto, applicantId, userId);
    }

    public void leaveCrew(Long crewId, Long userId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.leaveCrew(crewDto, userId);
    }

    public void expelMember(Long crewId, Long memberId, Long userId) {
        CrewDto crewDto = crewService.getCrewDtoById(crewId);
        crewService.expelMember(crewDto, memberId, userId);
    }

    private Page<CrewsResponse> convertCrewDtosToCrewsResponses(Page<CrewDto> crewDtos) {
        return crewDtos.map(crewDto -> {
            List<String> userImages = crewService.getUserImagesByCrew(crewDto);
            return CrewsResponse.of(crewDto, userImages);
        });
    }
}