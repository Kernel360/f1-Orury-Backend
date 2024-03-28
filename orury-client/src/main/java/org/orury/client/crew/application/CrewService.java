package org.orury.client.crew.application;

import org.orury.client.crew.interfaces.message.CrewMessage;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CrewService {
    CrewDto getCrewDtoById(Long crewId);

    void createCrew(CrewDto crewDto, MultipartFile image);

    Page<CrewDto> getCrewDtosByRank(Pageable pageable);

    Page<CrewDto> getCrewDtosByRecommend(Pageable pageable);

    Page<CrewDto> getCrewDtosByUserId(Long userId, Pageable pageable);

    List<String> getUserImagesByCrew(CrewDto crewDto, int maximumCount);

    boolean existCrewMember(Long crewId, Long userId);

    void updateCrewInfo(CrewDto oldCrew, CrewDto newCrew, Long userId);

    void updateCrewImage(CrewDto crewDto, MultipartFile image, Long userId);

    void deleteCrew(CrewDto crewDto, Long userId);

    CrewMessage applyCrew(CrewDto crewDto, UserDto userDto, String answer);

    void withdrawApplication(CrewDto crewDto, Long userId);

    void approveApplication(CrewDto crewDto, Long applicantId, Long userId);

    void disapproveApplication(CrewDto crewDto, Long applicantId, Long userId);

    void leaveCrew(CrewDto crewDto, Long userId);

    void expelMember(CrewDto crewDto, Long memberId, Long userId);
}
