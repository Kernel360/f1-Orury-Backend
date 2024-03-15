package org.orury.client.meeting.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.crew.application.CrewService;
import org.orury.client.gym.application.GymService;
import org.orury.client.meeting.interfaces.request.MeetingCreateRequest;
import org.orury.client.meeting.interfaces.request.MeetingUpdateRequest;
import org.orury.client.user.application.UserService;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.global.domain.Region;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Facade] 일정 Facade 테스트")
@ActiveProfiles("test")
class MeetingFacadeTest {
    private MeetingFacade meetingFacade;
    private MeetingService meetingService;
    private CrewService crewService;
    private UserService userService;
    private GymService gymService;

    @BeforeEach
    void setUp() {
        meetingService = mock(MeetingService.class);
        crewService = mock(CrewService.class);
        userService = mock(UserService.class);
        gymService = mock(GymService.class);

        meetingFacade = new MeetingFacade(meetingService, crewService, userService, gymService);
    }

    @DisplayName("일정생성Request와 유저id를 받으면, 일정을 생성한다.")
    @Test
    void should_CreateMeeting() {
        // given
        Long userId = 31L;
        MeetingCreateRequest request = createMeetingCreateRequest();
        UserDto userDto = createUserDto();
        GymDto gymDto = createGymDto();
        CrewDto crewDto = createCrewDto();
        given(userService.getUserDtoById(userId))
                .willReturn(userDto);
        given(gymService.getGymDtoById(anyLong()))
                .willReturn(gymDto);
        given(crewService.getCrewDtoById(anyLong()))
                .willReturn(crewDto);

        // when
        meetingFacade.createMeeting(request, userId);

        // then
        then(userService).should(only())
                .getUserDtoById(anyLong());
        then(gymService).should(only())
                .getGymDtoById(anyLong());
        then(crewService).should(only())
                .getCrewDtoById(anyLong());
        then(meetingService).should(only())
                .createMeeting(any());
    }

    @DisplayName("크루id와 유저id를 받으면, 현재일정목록을 조회한다.")
    @Test
    void should_GetPresentMeetingList() {
        // given
        Long crewId = 321L;
        Long userId = 123L;
        List<MeetingDto> meetingDtos = List.of(createMeetingDto(), createMeetingDto(), createMeetingDto());
        List<String> userImages = List.of("image1", "image2", "image3", "image4");
        given(meetingService.getPresentMeetingDtosByCrewId(crewId, userId))
                .willReturn(meetingDtos);
        given(meetingService.getUserImagesByMeeting(any(MeetingDto.class)))
                .willReturn(userImages);

        // when
        meetingFacade.getPresentMeetings(crewId, userId);

        // then
        then(meetingService).should(times(1))
                .getPresentMeetingDtosByCrewId(anyLong(), anyLong());
        then(meetingService).should(times(meetingDtos.size()))
                .getUserImagesByMeeting(any());
    }

    @DisplayName("크루id와 유저id를 받으면, 과거일정목록을 조회한다.")
    @Test
    void should_GetPastMeetingList() {
        // given
        Long crewId = 321L;
        Long userId = 123L;
        List<MeetingDto> meetingDtos = List.of(createMeetingDto(), createMeetingDto(), createMeetingDto());
        List<String> userImages = List.of("image1", "image2", "image3", "image4");
        given(meetingService.getPastMeetingDtosByCrewId(crewId, userId))
                .willReturn(meetingDtos);
        given(meetingService.getUserImagesByMeeting(any(MeetingDto.class)))
                .willReturn(userImages);

        // when
        meetingFacade.getPastMeetings(crewId, userId);

        // then
        then(meetingService).should(times(1))
                .getPastMeetingDtosByCrewId(anyLong(), anyLong());
        then(meetingService).should(times(meetingDtos.size()))
                .getUserImagesByMeeting(any());
    }

    @DisplayName("일정변경Request와 유저id를 받으면, 일정을 변경한다.")
    @Test
    void should_UpdateMeeting() {
        // given
        Long userId = 456L;
        MeetingUpdateRequest request = createMeetingUpdateRequest();
        MeetingDto meetingDto = createMeetingDto();
        GymDto gymDto = createGymDto();
        given(meetingService.getMeetingDtoById(anyLong()))
                .willReturn(meetingDto);
        given(gymService.getGymDtoById(anyLong()))
                .willReturn(gymDto);

        // when
        meetingFacade.updateMeeting(request, userId);

        // then
        then(meetingService).should(times(1))
                .getMeetingDtoById(anyLong());
        then(gymService).should(only())
                .getGymDtoById(anyLong());
        then(meetingService).should(times(1))
                .updateMeeting(any(), anyLong());
    }

    @DisplayName("일정id와 유저id를 받으면, 일정을 삭제한다.")
    @Test
    void should_DeleteMeeting() {
        // given
        Long meetingId = 645L;
        Long userId = 90L;
        MeetingDto meetingDto = createMeetingDto();
        given(meetingService.getMeetingDtoById(meetingId))
                .willReturn(meetingDto);

        // when
        meetingFacade.deleteMeeting(meetingId, userId);

        // then
        then(meetingService).should(times(1))
                .getMeetingDtoById(anyLong());
        then(meetingService).should(times(1))
                .deleteMeeting(any(), anyLong());
    }

    @DisplayName("일정id와 유저id를 받으면, 일정멤버에 추가한다.")
    @Test
    void should_AddMeetingMember() {
        // given
        Long meetingId = 77L;
        Long userId = 10L;
        MeetingDto meetingDto = createMeetingDto();
        given(meetingService.getMeetingDtoById(meetingId))
                .willReturn(meetingDto);

        // when
        meetingFacade.addMeetingMember(meetingId, userId);

        // then
        then(meetingService).should(times(1))
                .getMeetingDtoById(anyLong());
        then(meetingService).should(times(1))
                .addMeetingMember(any(), anyLong());
    }

    @DisplayName("일정id와 유저id를 받으면, 일정멤버에 삭제한다.")
    @Test
    void should_RemoveMeetingMember() {
        // given
        Long meetingId = 77L;
        Long userId = 10L;
        MeetingDto meetingDto = createMeetingDto();
        given(meetingService.getMeetingDtoById(meetingId))
                .willReturn(meetingDto);

        // when
        meetingFacade.removeMeetingMember(meetingId, userId);

        // then
        then(meetingService).should(times(1))
                .getMeetingDtoById(anyLong());
        then(meetingService).should(times(1))
                .removeMeetingMember(any(), anyLong());
    }

    @DisplayName("일정id와 유저id를 받으면, 일정멤버 목록을 조회한다.")
    @Test
    void should_GetMeetingMemberList() {
        // given
        Long meetingId = 77L;
        Long userId = 10L;
        MeetingDto meetingDto = createMeetingDto();
        List<UserDto> userDtos = List.of(createUserDto(), createUserDto(), createUserDto());
        given(meetingService.getMeetingDtoById(meetingId))
                .willReturn(meetingDto);
        given(meetingService.getUserDtosByMeeting(meetingDto, userId))
                .willReturn(userDtos);

        // when
        meetingFacade.getMeetingMembers(meetingId, userId);

        // then
        then(meetingService).should(times(1))
                .getMeetingDtoById(anyLong());
        then(meetingService).should(times(1))
                .getUserDtosByMeeting(any(), anyLong());
    }

    private UserDto createUserDto() {
        return UserDto.of(
                111L,
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

    private GymDto createGymDto() {
        return GymDto.of(
                222L,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                4.5f,
                12,
                11,
                List.of("image1"),
                37.513709,
                127.062144,
                "더클라임",
                "01012345678",
                "gymInstagramLink.com",
                "MONDAY",
                LocalDateTime.of(1999, 3, 1, 7, 30),
                LocalDateTime.of(2024, 1, 23, 18, 32),
                "11:11-23:11",
                "11:22-23:22",
                "11:33-23:33",
                "11:44-23:44",
                "11:55-23:55",
                "11:66-23:66",
                "11:77-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private CrewDto createCrewDto() {
        return CrewDto.of(
                333L,
                "테스트크루",
                12,
                30,
                Region.강남구,
                "크루 설명",
                "orury/crew/crew_icon",
                CrewStatus.ACTIVATED,
                createUserDto(),
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

    private MeetingDto createMeetingDto() {
        return MeetingDto.of(
                444L,
                LocalDateTime.of(2222, 3, 14, 18, 32),
                1,
                5,
                createUserDto(),
                createGymDto(),
                createCrewDto(),
                true,
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32)
        );
    }

    private MeetingCreateRequest createMeetingCreateRequest() {
        return MeetingCreateRequest.of(
                LocalDateTime.of(2222, 3, 14, 18, 32),
                5,
                222L,
                333L
        );
    }

    private MeetingUpdateRequest createMeetingUpdateRequest() {
        return new MeetingUpdateRequest(
                444L,
                LocalDateTime.of(2222, 3, 14, 18, 32),
                10,
                333L
        );
    }
}
