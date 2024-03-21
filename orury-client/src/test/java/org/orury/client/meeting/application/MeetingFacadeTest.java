package org.orury.client.meeting.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.config.FacadeTest;
import org.orury.client.meeting.interfaces.request.MeetingCreateRequest;
import org.orury.client.meeting.interfaces.request.MeetingUpdateRequest;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.orury.client.ClientFixtureFactory.TestMeetingCreateRequest.createMeetingCreateRequest;
import static org.orury.client.ClientFixtureFactory.TestMeetingUpdateRequest.createMeetingUpdateRequest;
import static org.orury.domain.CrewDomainFixture.TestCrewDto.createCrewDto;
import static org.orury.domain.GymDomainFixture.TestGymDto.createGymDto;
import static org.orury.domain.MeetingDomainFixture.TestMeetingDto.createMeetingDto;
import static org.orury.domain.UserDomainFixture.TestUserDto.createUserDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Facade] 일정 Facade 테스트")
@ActiveProfiles("test")
class MeetingFacadeTest extends FacadeTest {

    @DisplayName("일정생성Request와 유저id를 받으면, 일정을 생성한다.")
    @Test
    void should_CreateMeeting() {
        // given
        Long userId = 31L;
        MeetingCreateRequest request = createMeetingCreateRequest().build().get();
        UserDto userDto = createUserDto().build().get();
        GymDto gymDto = createGymDto().build().get();
        CrewDto crewDto = createCrewDto().build().get();
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
        List<MeetingDto> meetingDtos = List.of(createMeetingDto().build().get(), createMeetingDto().build().get(), createMeetingDto().build().get());
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
        List<MeetingDto> meetingDtos = List.of(createMeetingDto().build().get(), createMeetingDto().build().get(), createMeetingDto().build().get());
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
        MeetingUpdateRequest request = createMeetingUpdateRequest().build().get();
        MeetingDto meetingDto = createMeetingDto().build().get();
        GymDto gymDto = createGymDto().build().get();
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
        MeetingDto meetingDto = createMeetingDto().build().get();
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
        MeetingDto meetingDto = createMeetingDto().build().get();
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
        MeetingDto meetingDto = createMeetingDto().build().get();
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
        MeetingDto meetingDto = createMeetingDto().build().get();
        List<UserDto> userDtos = List.of(createUserDto().build().get(), createUserDto().build().get(), createUserDto().build().get());
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
}
