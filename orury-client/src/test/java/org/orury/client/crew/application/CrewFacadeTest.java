package org.orury.client.crew.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.FacadeTest;
import org.orury.client.crew.interfaces.request.CrewRequest;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.domain.Region;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.only;

@DisplayName("[Facade] 크루 Facade 테스트")
class CrewFacadeTest extends FacadeTest {

    @DisplayName("크루생성Request, 크루image 유저id를 받으면, 크루를 생성한다.")
    @Test
    void should_CreateCrew() {
        // given
        CrewRequest request = createCrewRequest();
        MultipartFile image = mock(MultipartFile.class);
        Long userId = 1L;
        UserDto userDto = createUserDto(userId);
        given(userService.getUserDtoById(anyLong()))
                .willReturn(userDto);

        // when
        crewFacade.createCrew(request, image, userId);

        // then
        then(userService).should(only())
                .getUserDtoById(anyLong());
        then(crewService).should(only())
                .createCrew(any(CrewDto.class), any(MultipartFile.class));
    }

    @DisplayName("페이지번호를 받으면, 크루랭크에 따른 크루 목록을 반환한다.")
    @Test
    void should_GetCrewsByRank() {
        // given
        int page = 2;
        List<CrewDto> crewDtos = List.of(createCrewDto(3L), createCrewDto(4L));
        Page<CrewDto> crewDtoPage = new PageImpl<>(crewDtos, PageRequest.of(page, 10), 2);
        given(crewService.getCrewDtosByRank(any()))
                .willReturn(crewDtoPage);
        given(crewService.getUserImagesByCrew(any(CrewDto.class)))
                .willReturn(mock(List.class));

        // when
        crewFacade.getCrewsByRank(page);

        // then
        then(crewService).should(times(1))
                .getCrewDtosByRank(any());
        then(crewService).should(times(crewDtos.size()))
                .getUserImagesByCrew(any());
    }

    @DisplayName("페이지번호를 받으면, 크루추천에 따른 크루 목록을 반환한다.")
    @Test
    void should_GetCrewsByRecommend() {
        // given
        int page = 2;
        List<CrewDto> crewDtos = List.of(createCrewDto(3L), createCrewDto(4L));
        Page<CrewDto> crewDtoPage = new PageImpl<>(crewDtos, PageRequest.of(page, 10), 2);
        given(crewService.getCrewDtosByRecommend(any()))
                .willReturn(crewDtoPage);
        given(crewService.getUserImagesByCrew(any(CrewDto.class)))
                .willReturn(mock(List.class));

        // when
        crewFacade.getCrewsByRecommend(page);

        // then
        then(crewService).should(times(1))
                .getCrewDtosByRecommend(any());
        then(crewService).should(times(crewDtos.size()))
                .getUserImagesByCrew(any());
    }

    @DisplayName("페이지번호를 받으면, 유저id에 따른 크루 목록을 반환한다.")
    @Test
    void should_GetMyCrews() {
        // given
        Long userId = 23L;
        int page = 2;
        List<CrewDto> crewDtos = List.of(createCrewDto(3L), createCrewDto(4L));
        Page<CrewDto> crewDtoPage = new PageImpl<>(crewDtos, PageRequest.of(page, 10), 2);
        given(crewService.getCrewDtosByUserId(anyLong(), any()))
                .willReturn(crewDtoPage);
        given(crewService.getUserImagesByCrew(any(CrewDto.class)))
                .willReturn(mock(List.class));

        // when
        crewFacade.getMyCrews(userId, page);

        // then
        then(crewService).should(times(1))
                .getCrewDtosByUserId(anyLong(), any());
        then(crewService).should(times(crewDtos.size()))
                .getUserImagesByCrew(any());
    }

    @DisplayName("유저id, 크루id를 받으면, 크루정보를 반환한다.")
    @Test
    void should_GetCrewByCrewId() {
        // given
        Long userId = 23L;
        Long crewId = 3L;
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(createCrewDto(crewId));
        given(crewService.existCrewMember(any(CrewMemberPK.class)))
                .willReturn(anyBoolean());

        // when
        crewFacade.getCrewByCrewId(userId, crewId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .existCrewMember(any());
    }

    @DisplayName("크루id, 크루정보Request, 유저id를 받으면, 크루정보를 업데이트한다.")
    @Test
    void should_UpdateCrewInfo() {
        // given
        Long crewId = 3L;
        Long userId = 23L;
        CrewRequest request = createCrewRequest();
        CrewDto oldCrewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(oldCrewDto);

        // when
        crewFacade.updateCrewInfo(crewId, request, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .updateCrewInfo(any(), any(), anyLong());
    }

    @DisplayName("크루id, 크루image, 유저id를 받으면, 크루이미지를 업데이트한다.")
    @Test
    void should_UpdateCrewImage() {
        // given
        Long crewId = 3L;
        Long userId = 23L;
        MultipartFile image = mock(MultipartFile.class);
        CrewDto crewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        crewFacade.updateCrewImage(crewId, image, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .updateCrewImage(any(), any(), anyLong());
    }

    @DisplayName("크루id, 유저id를 받으면, 크루를 삭제한다.")
    @Test
    void should_DeleteCrew() {
        // given
        Long crewId = 3L;
        Long userId = 23L;
        CrewDto crewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        crewFacade.deleteCrew(crewId, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .deleteCrew(any(), anyLong());
    }

    @DisplayName("크루id, 유저id, 답변을 받으면, 크루에 가입신청한다.")
    @Test
    void should_ApplyCrew() {
        // given
        Long crewId = 3L;
        Long userId = 23L;
        String answer = "답변";
        CrewDto crewDto = createCrewDto(crewId);
        UserDto userDto = createUserDto(userId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);
        given(userService.getUserDtoById(anyLong()))
                .willReturn(userDto);

        // when
        crewFacade.applyCrew(crewId, userId, answer);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(userService).should(only())
                .getUserDtoById(anyLong());
        then(crewService).should(times(1))
                .applyCrew(any(), any(), any());
    }

    @DisplayName("크루id, 유저id를 받으면, 크루가입신청을 취소한다.")
    @Test
    void should_WithdrawApplication() {
        // given
        Long crewId = 3L;
        Long userId = 23L;
        CrewDto crewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        crewFacade.withdrawApplication(crewId, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .withdrawApplication(any(), anyLong());
    }

    @DisplayName("크루id, 지원자id, 유저id를 받아, 크루가입신청을 승인한다.")
    @Test
    void should_ApproveApplication() {
        // given
        Long crewId = 3L;
        Long applicantId = 23L;
        Long userId = 24L;
        CrewDto crewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        crewFacade.approveApplication(crewId, applicantId, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .approveApplication(any(), anyLong(), anyLong());
    }

    @DisplayName("크루id, 지원자id, 유저id를 받아, 크루가입신청을 거절한다.")
    @Test
    void should_DisapproveApplication() {
        // given
        Long crewId = 3L;
        Long applicantId = 23L;
        Long userId = 24L;
        CrewDto crewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        crewFacade.disapproveApplication(crewId, applicantId, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .disapproveApplication(any(), anyLong(), anyLong());
    }

    @DisplayName("크루id, 유저id를 받아, 크루를 탈퇴한다.")
    @Test
    void should_LeaveCrew() {
        // given
        Long crewId = 3L;
        Long userId = 23L;
        CrewDto crewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        crewFacade.leaveCrew(crewId, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .leaveCrew(any(), anyLong());
    }

    @DisplayName("크루id, 멤버id, 유저id를 받아, 멤버를 강퇴한다.")
    @Test
    void should_ExpelMember() {
        // given
        Long crewId = 3L;
        Long memberId = 23L;
        Long userId = 24L;
        CrewDto crewDto = createCrewDto(crewId);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        crewFacade.expelMember(crewId, memberId, userId);

        // then
        then(crewService).should(times(1))
                .getCrewDtoById(anyLong());
        then(crewService).should(times(1))
                .expelMember(any(), anyLong(), anyLong());
    }

    private UserDto createUserDto(Long userId) {
        return UserDto.of(
                userId,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                UserStatus.ENABLE
        );
    }

    private CrewRequest createCrewRequest() {
        return CrewRequest.of(
                "테스트크루",
                12,
                Region.강남구,
                "크루 설명",
                15,
                35,
                CrewGender.ANY,
                false,
                null,
                false,
                List.of("크루태그1", "크루태그2")
        );
    }

    private CrewDto createCrewDto(Long crewId) {
        return CrewDto.of(
                crewId,
                "테스트크루",
                12,
                30,
                Region.강남구,
                "크루 설명",
                "orury/crew/crew_icon",
                CrewStatus.ACTIVATED,
                createUserDto(23L),
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32),
                15,
                35,
                CrewGender.ANY,
                false,
                null,
                false,
                List.of("크루태그1", "크루태그2")
        );
    }
}