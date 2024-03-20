package org.orury.client.meeting.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ServiceTest;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.code.MeetingErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.global.domain.Region;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.meeting.domain.entity.MeetingMemberPK;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DisplayName("[Service] 일정 ServiceImpl 테스트")
class MeetingServiceImplTest extends ServiceTest {

    @DisplayName("[getMeetingDtoById] 존재하는 일정id를 받으면, 일정Dto를 반환한다.")
    @Test
    void when_ExistingMeetingId_Then_RetreiveMeetingDto() {
        // given
        Long meetingId = 12L;
        Meeting meeting = createMeeting(meetingId);
        given(meetingReader.findById(meetingId))
                .willReturn(Optional.of(meeting));

        // when
        meetingService.getMeetingDtoById(meetingId);

        // then
        then(meetingReader).should(only())
                .findById(anyLong());
    }

    @DisplayName("[createMeeting] 존재하지 않는 일정id를 받으면, NotFound 예외를 반환한다.")
    @Test
    void when_NotExistingMeetingId_Then_NotFoundException() {
        // given
        Long meetingId = 11L;
        given(meetingReader.findById(meetingId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class,
                () -> meetingService.getMeetingDtoById(meetingId));

        then(meetingReader).should(only())
                .findById(anyLong());
    }

    @DisplayName("[createMeeting] 일정dto를 받으면, 일정을 생성하고 일정멤버에 일정생성자를 추가한다.")
    @Test
    void when_validMeetingDto_Then_CreateMeetingAndAddMeetingMember() {
        // given
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int validCapacity = 3;
        MeetingDto meetingDto = createMeetingDto(startTime, validCapacity);
        Meeting meeting = createMeeting(meetingDto.id());
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);
        given(meetingStore.createMeeting(any()))
                .willReturn(meeting);

        // when
        meetingService.createMeeting(meetingDto);

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingStore).should(only())
                .createMeeting(any());
        then(meetingMemberStore).should(only())
                .addMember(any());
    }

    @DisplayName("[createMeeting] 크루원이 아닌 일정dto를 받으면, NotCrewMember 예외를 반환한다.")
    @Test
    void when_NotCrewMember_Then_NotCrewMemberException() {
        // given
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int validCapacity = 3;
        MeetingDto meetingDto = createMeetingDto(startTime, validCapacity);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.createMeeting(meetingDto));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingStore).should(never())
                .createMeeting(any());
        then(meetingMemberStore).should(never())
                .addMember(any());
    }

    @DisplayName("[createMeeting] 시작시간이 과거인 일정dto를 받으면, InvalidStartTime 예외를 반환한다.")
    @Test
    void when_StartTimeIsPast_Then_InvalidStartException() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        int validCapacity = 3;
        MeetingDto meetingDto = createMeetingDto(startTime, validCapacity);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.createMeeting(meetingDto));

        assertEquals(MeetingErrorCode.INVALID_START_TIME.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingStore).should(never())
                .createMeeting(any());
        then(meetingMemberStore).should(never())
                .addMember(any());
    }

    @DisplayName("[createMeeting] 최소인원(2명)보다 적은 정원의 일정dto를 받으면, InvalidCapacity 예외를 반환한다.")
    @Test
    void when_LessThanMinimum_Then_InvalidCapacityException() {
        // given
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int invalidCapacity = 1;
        MeetingDto meetingDto = createMeetingDto(startTime, invalidCapacity);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.createMeeting(meetingDto));

        assertEquals(MeetingErrorCode.INVALID_CAPACITY.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingStore).should(never())
                .createMeeting(any());
        then(meetingMemberStore).should(never())
                .addMember(any());
    }

    @DisplayName("[createMeeting] 크루전체인원보다 많은 정원의 일정dto를 받으면, InvalidCapacity 예외를 반환한다.")
    @Test
    void when_MoreThanCrewMemberCount_Then_InvalidCapacityException() {
        // given
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int invalidCapacity = 1000;
        MeetingDto meetingDto = createMeetingDto(startTime, invalidCapacity);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.createMeeting(meetingDto));

        assertEquals(MeetingErrorCode.INVALID_CAPACITY.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingStore).should(never())
                .createMeeting(any());
        then(meetingMemberStore).should(never())
                .addMember(any());
    }

    @DisplayName("[getPresentMeetingDtosByCrewId] 크루id와 유저id를 받으면, 예정된 일정Dto리스트를 반환한다.")
    @Test
    void when_CrewIdAndUserId_Then_RetrieveNotStartedMeetingDtos() {
        // given
        Long crewId = 3L;
        Long userId = 1L;
        List<Meeting> meetings = List.of(createMeeting(3L), createMeeting(2L), createMeeting(1L));
        given(crewMemberReader.existsByCrewIdAndUserId(crewId, userId))
                .willReturn(true);
        given(meetingReader.getNotStartedMeetingsByCrewId(crewId))
                .willReturn(meetings);
        given(meetingMemberReader.existsByMeetingIdAndUserId(anyLong(), anyLong()))
                .willReturn(true, false, false);

        // when
        meetingService.getPresentMeetingDtosByCrewId(crewId, userId);

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingReader).should(only())
                .getNotStartedMeetingsByCrewId(anyLong());
        then(meetingMemberReader).should(times(meetings.size()))
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
    }

    @DisplayName("[getPresentMeetingDtosByCrewId] 크루원이 아닌 유저의 id를 받으면, NotCrewMember 예외를 반환한다.")
    @Test
    void when_UserIsNotCrewMember_Then_NotCrewMemberException1() {
        // given
        Long crewId = 3L;
        Long userId = 1L;
        given(crewMemberReader.existsByCrewIdAndUserId(crewId, userId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.getPresentMeetingDtosByCrewId(crewId, userId));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingReader).should(never())
                .getNotStartedMeetingsByCrewId(anyLong());
        then(meetingMemberReader).should(never())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
    }

    @DisplayName("[getPastMeetingDtosByCrewId] 크루id와 유저id를 받으면, 예정된 일정Dto리스트를 반환한다.")
    @Test
    void when_CrewIdAndUserId_Then_RetrievePastMeetingDtos() {
        // given
        Long crewId = 3L;
        Long userId = 1L;
        List<Meeting> meetings = List.of(createMeeting(3L), createMeeting(2L), createMeeting(1L));
        given(crewMemberReader.existsByCrewIdAndUserId(crewId, userId))
                .willReturn(true);
        given(meetingReader.getStartedMeetingsByCrewId(crewId))
                .willReturn(meetings);
        given(meetingMemberReader.existsByMeetingIdAndUserId(anyLong(), anyLong()))
                .willReturn(true, false, false);

        // when
        meetingService.getPastMeetingDtosByCrewId(crewId, userId);

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingReader).should(only())
                .getStartedMeetingsByCrewId(anyLong());
        then(meetingMemberReader).should(times(meetings.size()))
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
    }

    @DisplayName("[getPastMeetingDtosByCrewId] 크루원이 아닌 유저의 id를 받으면, NotCrewMember 예외를 반환한다.")
    @Test
    void when_UserIsNotCrewMember_Then_NotCrewMemberException2() {
        // given
        Long crewId = 3L;
        Long userId = 1L;
        given(crewMemberReader.existsByCrewIdAndUserId(crewId, userId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.getPastMeetingDtosByCrewId(crewId, userId));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingReader).should(never())
                .getNotStartedMeetingsByCrewId(anyLong());
        then(meetingMemberReader).should(never())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
    }

    @DisplayName("[getUserImagesByMeeting] 일정dto를 받으면, 일정에 참여한 유저들의 프로필사진목록을 반환한다.")
    @Test
    void when_MeetingDto_Then_RetrieveUserImages() {
        // given
        Long meetingId = 8L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        List<MeetingMember> otherMembers = List.of(
                createMeetingMember(12L, meetingId),
                createMeetingMember(34L, meetingId),
                createMeetingMember(56L, meetingId)
        );
        given(meetingMemberReader.getOtherMeetingMembersByMeetingIdMaximum(anyLong(), anyLong(), anyInt()))
                .willReturn(otherMembers);
        given(userReader.getUserById(anyLong()))
                .willReturn(createUser(12L), createUser(34L), createUser(56L));

        // when
        List<String> userImages = meetingService.getUserImagesByMeeting(meetingDto);

        // then
        assertEquals(meetingDto.userDto().profileImage(), userImages.get(0));
        assertEquals(otherMembers.size() + 1, userImages.size());
        then(meetingMemberReader).should(only())
                .getOtherMeetingMembersByMeetingIdMaximum(anyLong(), anyLong(), anyInt());
        then(userReader).should(times(otherMembers.size()))
                .getUserById(anyLong());
    }

    @DisplayName("[getUserImagesByMeeting] 일정에 참여한 다른 멤버가 없어도, 일정을 주최한 멤버의 프로필사진을 리스트에 담아 반환한다.")
    @Test
    void when_OnlyOneMemberInMeeting_Then_RetrieveUserImage() {
        // given
        Long meetingId = 8L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        given(meetingMemberReader.getOtherMeetingMembersByMeetingIdMaximum(anyLong(), anyLong(), anyInt()))
                .willReturn(Collections.emptyList());

        // when
        List<String> userImages = meetingService.getUserImagesByMeeting(meetingDto);

        // then
        assertEquals(meetingDto.userDto().profileImage(), userImages.get(0));
        assertEquals(1, userImages.size());
        then(meetingMemberReader).should(only())
                .getOtherMeetingMembersByMeetingIdMaximum(anyLong(), anyLong(), anyInt());
        then(userReader).should(never())
                .getUserById(anyLong());
    }

    @DisplayName("[updateMeeting] 변경하는 일정dto와 유저id가 들어오면, 일정 정보를 변경한다.")
    @Test
    void when_MeetingDtoAndUserId_Then_UpdateMeeting() {
        // given
        Long userId = 24L;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int validCapacity = 5;
        int memberCount = 3;
        MeetingDto meetingDto = createMeetingDto(userId, startTime, validCapacity, memberCount);

        // when
        meetingService.updateMeeting(meetingDto, userId);

        // then
        then(meetingStore).should(only())
                .updateMeeting(any());
    }

    @DisplayName("[updateMeeting] 변경하는 일정의 유저가 아니면, Forbidden 예외를 반환한다.")
    @Test
    void when_NotMeetingCreator_Then_ForbiddenException() {
        // given
        Long userId = 24L;
        Long otherUserId = 422L;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int validCapacity = 5;
        int memberCount = 3;
        MeetingDto meetingDto = createMeetingDto(otherUserId, startTime, validCapacity, memberCount);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.updateMeeting(meetingDto, userId));

        assertEquals(MeetingErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
        then(meetingStore).should(never())
                .updateMeeting(any());
    }

    @DisplayName("[updateMeeting] 시작시간이 과거인 일정dto를 받으면, InvalidStartTime 예외를 반환한다.")
    @Test
    void when_UpdatingStartTimeIsPast_Then_InvalidStartException() {
        // given
        Long userId = 24L;
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        int validCapacity = 5;
        int memberCount = 3;
        MeetingDto meetingDto = createMeetingDto(userId, startTime, validCapacity, memberCount);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.updateMeeting(meetingDto, userId));

        assertEquals(MeetingErrorCode.INVALID_START_TIME.getMessage(), exception.getMessage());
        then(meetingStore).should(never())
                .updateMeeting(any());
    }

    @DisplayName("[updateMeeting] 최소인원(2명)보다 정원이 적으면, InvalidCapacity 예외를 반환한다.")
    @Test
    void when_UpdatingCapacityIsLessThanMinimum_Then_InvalidCapacityException() {
        // given
        Long userId = 24L;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int invalidCapacity = 1;
        int memberCount = 1;
        MeetingDto meetingDto = createMeetingDto(userId, startTime, invalidCapacity, memberCount);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.updateMeeting(meetingDto, userId));

        assertEquals(MeetingErrorCode.INVALID_CAPACITY.getMessage(), exception.getMessage());
        then(meetingStore).should(never())
                .updateMeeting(any());
    }

    @DisplayName("[updateMeeting] 크루전체인원보다 정원이 많으면, InvalidCapacity 예외를 반환한다.")
    @Test
    void when_UpdatingCapacityIsMoreThanCrewMemberCount_Then_InvalidCapacityException() {
        // given
        Long userId = 24L;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int invalidCapacity = 500;
        int memberCount = 3;
        MeetingDto meetingDto = createMeetingDto(userId, startTime, invalidCapacity, memberCount);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.updateMeeting(meetingDto, userId));

        assertEquals(MeetingErrorCode.INVALID_CAPACITY.getMessage(), exception.getMessage());
        then(meetingStore).should(never())
                .updateMeeting(any());
    }

    @DisplayName("[updateMeeting] 일정에 참여하고 있는 인원보다 정원이 적으면, CapacityForbidden 예외를 반환한다.")
    @Test
    void when_updatingCapacityIsLessThanMeetingMemberCount_Then_CapacityForbidden() {
        // given
        Long userId = 24L;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        int invalidCapacity = 2;
        int memberCount = 3;
        MeetingDto meetingDto = createMeetingDto(userId, startTime, invalidCapacity, memberCount);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.updateMeeting(meetingDto, userId));

        assertEquals(MeetingErrorCode.CAPACITY_FORBIDDEN.getMessage(), exception.getMessage());
        then(meetingStore).should(never())
                .updateMeeting(any());
    }

    @DisplayName("[deleteMeeting] 삭제하는 일정dto와 유저id가 들어오면, 일정을 삭제한다.")
    @Test
    void when_MeetingDtoAndUserId_Then_DeleteMeeting() {
        // given
        Long userId = 6L;
        MeetingDto meetingDto = createMeetingDtoByUserId(userId);

        // when
        meetingService.deleteMeeting(meetingDto, userId);

        // then
        then(meetingStore).should(only())
                .deleteMeeting(any());
    }

    @DisplayName("[deleteMeeting] 변경하는 일정의 유저가 아니면, Forbidden 예외를 반환한다.")
    @Test
    void when_NotMeetingCreator_Then_ForbiddenExceptionForDeletingMeeting() {
        // given
        Long userId = 6L;
        Long otherUserId = 215L;
        MeetingDto meetingDto = createMeetingDtoByUserId(otherUserId);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.deleteMeeting(meetingDto, userId));

        assertEquals(MeetingErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
        then(meetingStore).should(never())
                .deleteMeeting(any());
    }

    @DisplayName("[addMeetingMember] 일정dto와 유저id를 받아, 일정에 멤버를 추가한다.")
    @Test
    void when_MeetingDtoAnsUserId_Then_AddMeetingMember() {
        // given
        Long userId = 26L;
        Long meetingId = 5L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);
        given(meetingMemberReader.existsByMeetingIdAndUserId(anyLong(), anyLong()))
                .willReturn(false);

        // when
        meetingService.addMeetingMember(meetingDto, userId);

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(only())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(only())
                .addMember(any(MeetingMember.class));
    }

    @DisplayName("[addMeetingMember] 크루원이 아니면, NotCrewMember 예외를 반환한다.")
    @Test
    void when_AddingNotCrewMember_Then_NotCrewMemberException() {
        // given
        Long userId = 26L;
        Long meetingId = 5L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.addMeetingMember(meetingDto, userId));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(never())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(never())
                .addMember(any());
    }

    @DisplayName("[addMeetingMember] 해당 일정에 이미 참여하고 있는 멤버면, AlreadyJoinedMeeting 예외를 반환한다.")
    @Test
    void when_AlreadyJoinedMember_Then_AlreadyJoinedMeetingException() {
        // given
        Long userId = 26L;
        Long meetingId = 5L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);
        given(meetingMemberReader.existsByMeetingIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.addMeetingMember(meetingDto, userId));

        assertEquals(MeetingErrorCode.ALREADY_JOINED_MEETING.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(only())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(never())
                .addMember(any());
    }

    @DisplayName("[addMeetingMember] 일정에 정원이 다 찼다면, FullMeeting 예외를 반환한다.")
    @Test
    void when_FullMeeting_Then_FullMeetingException() {
        // given
        Long userId = 26L;
        Long meetingId = 5L;
        MeetingDto meetingDto = createFullMeetingDto(meetingId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);
        given(meetingMemberReader.existsByMeetingIdAndUserId(anyLong(), anyLong()))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.addMeetingMember(meetingDto, userId));

        assertEquals(MeetingErrorCode.FULL_MEETING.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(only())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(never())
                .addMember(any());
    }

    @DisplayName("[removeMeetingMember] 일정dto와 유저id를 받아, 일정에서 멤버를 제거한다.")
    @Test
    void when_MeetingDtoAnsUserId_Then_RemoveMeetingMember() {
        // given
        Long userId = 26L;
        Long meetingId = 5L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);
        given(meetingMemberReader.existsByMeetingIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // when
        meetingService.removeMeetingMember(meetingDto, userId);

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(only())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(only())
                .removeMember(any(MeetingMember.class));
    }

    @DisplayName("[removeMeetingMember] 크루원이 아니면, NotCrewMember 예외를 반환한다.")
    @Test
    void when_RemovingNotCrewMember_Then_NotCrewMemberException() {
        // given
        Long userId = 26L;
        Long meetingId = 5L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.removeMeetingMember(meetingDto, userId));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(never())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(never())
                .removeMember(any(MeetingMember.class));
    }

    @DisplayName("[removeMeetingMember] 제거하려는 멤버가 일정 주최자면, MeetingCreator 예외를 반환한다.")
    @Test
    void when_RemovingMeetingCreator_Then_MeetingCreatorException() {
        Long userId = 26L;
        MeetingDto meetingDto = createMeetingDtoByUserId(userId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.removeMeetingMember(meetingDto, userId));

        assertEquals(MeetingErrorCode.MEETING_CREATOR.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(never())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(never())
                .removeMember(any(MeetingMember.class));
    }

    @DisplayName("[removeMeetingMember] 제거하려는 멤버가 일정에 존재하지 않으면, NotJoinedMeeting 예외를 반환한다.")
    @Test
    void when_NotExistingMeetingMember_Then_NotJoinedMeetingException() {
        Long userId = 26L;
        Long meetingId = 5L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);
        given(meetingMemberReader.existsByMeetingIdAndUserId(anyLong(), anyLong()))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> meetingService.removeMeetingMember(meetingDto, userId));

        assertEquals(MeetingErrorCode.NOT_JOINED_MEETING.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(only())
                .existsByMeetingIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(never())
                .removeMember(any(MeetingMember.class));
    }

    @DisplayName("[getUserDtosByMeeting] 일정dto와 유저id를 받아, 일정에 참여하는 유저Dto 리스트를 반환한다.")
    @Test
    void when_MeetingDtoAndUserId_Then_RetrieveUserDtosByMeeting() {
        // given
        Long userId = 1248L;
        Long meetingId = 54L;
        MeetingDto meetingDto = createMeetingDto(meetingId);
        List<MeetingMember> meetingMembers = List.of(
                createMeetingMember(4L, meetingId),
                createMeetingMember(14L, meetingId),
                createMeetingMember(24L, meetingId)
        );
        given(crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong()))
                .willReturn(true);
        given(meetingMemberReader.getMeetingMembersByMeetingId(anyLong()))
                .willReturn(meetingMembers);
        given(userReader.getUserById(anyLong()))
                .willReturn(createUser(4L), createUser(14L), createUser(24L));

        // when
        meetingService.getUserDtosByMeeting(meetingDto, userId);

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberReader).should(only())
                .getMeetingMembersByMeetingId(anyLong());
        then(userReader).should(times(meetingMembers.size()))
                .getUserById(anyLong());
    }


    private User createUser() {
        return User.of(
                1L,
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

    private User createUser(Long userId) {
        return User.of(
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

    private Gym createGym() {
        return Gym.of(
                60L,
                "gymName",
                "gymKakaoId",
                "gymRoadAddress",
                "gymAddress",
                40.5f,
                23,
                12,
                List.of(),
                123.456,
                123.456,
                "gymBrand",
                "010-1234-5678",
                "gymInstaLink",
                "MONDAY",
                "11:00-23:11",
                "12:00-23:22",
                "13:00-23:33",
                "14:00-23:44",
                "15:00-23:55",
                "16:00-23:66",
                "17:00-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private Crew createCrew() {
        return Crew.of(
                14L,
                "테스트크루",
                12,
                30,
                Region.강남구,
                "크루 설명",
                "orury/crew/crew_icon",
                CrewStatus.ACTIVATED,
                createUser(),
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32),
                15,
                35,
                CrewGender.ANY,
                false,
                null,
                false
        );
    }

    private Meeting createMeeting(Long meetingId) {
        return Meeting.of(
                meetingId,
                LocalDateTime.of(2222, 3, 14, 18, 32),
                1,
                5,
                createUser(),
                createGym(),
                createCrew(),
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32)
        );
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

    private MeetingDto createMeetingDto(Long meetingId) {
        return MeetingDto.of(
                meetingId,
                LocalDateTime.of(2024, 12, 9, 7, 30),
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

    private MeetingDto createFullMeetingDto(Long meetingId) {
        return MeetingDto.of(
                meetingId,
                LocalDateTime.of(2024, 12, 9, 7, 30),
                10,
                10,
                createUserDto(),
                createGymDto(),
                createCrewDto(),
                true,
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32)
        );
    }

    private MeetingDto createMeetingDtoByUserId(Long userId) {
        return MeetingDto.of(
                42632L,
                LocalDateTime.of(2024, 12, 9, 7, 30),
                1,
                5,
                createUserDto(userId),
                createGymDto(),
                createCrewDto(),
                true,
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32)
        );
    }

    private MeetingDto createMeetingDto(LocalDateTime startTime, int capacity) {
        return MeetingDto.of(
                444L,
                startTime,
                1,
                capacity,
                createUserDto(),
                createGymDto(),
                createCrewDto(),
                true,
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32)
        );
    }

    private MeetingDto createMeetingDto(Long userId, LocalDateTime startTime, int capacity, int memberCount) {
        return MeetingDto.of(
                444L,
                startTime,
                memberCount,
                capacity,
                createUserDto(userId),
                createGymDto(),
                createCrewDto(),
                true,
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32)
        );
    }

    private MeetingMember createMeetingMember(Long userId, Long meetingId) {
        MeetingMemberPK meetingMemberPK = MeetingMemberPK.of(userId, meetingId);
        return MeetingMember.of(meetingMemberPK);
    }
}
