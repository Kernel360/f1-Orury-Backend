package org.orury.client.crew.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.global.image.ImageAsyncStore;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.crew.domain.*;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.domain.Region;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.orury.domain.meeting.domain.MeetingStore;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 크루 ServiceImpl 테스트")
@ActiveProfiles("test")
class CrewServiceImplTest {
    private CrewServiceImpl crewService;
    private CrewReader crewReader;
    private CrewStore crewStore;
    private CrewTagReader crewTagReader;
    private CrewTagStore crewTagStore;
    private CrewMemberReader crewMemberReader;
    private CrewMemberStore crewMemberStore;
    private CrewApplicationReader crewApplicationReader;
    private CrewApplicationStore crewApplicationStore;
    private MeetingStore meetingStore;
    private MeetingMemberStore meetingMemberStore;
    private UserReader userReader;
    private ImageStore imageStore;
    private ImageAsyncStore imageAsyncStore;

    @BeforeEach
    void setUp() {
        crewReader = mock(CrewReader.class);
        crewStore = mock(CrewStore.class);
        crewTagReader = mock(CrewTagReader.class);
        crewTagStore = mock(CrewTagStore.class);
        crewMemberReader = mock(CrewMemberReader.class);
        crewMemberStore = mock(CrewMemberStore.class);
        crewApplicationReader = mock(CrewApplicationReader.class);
        crewApplicationStore = mock(CrewApplicationStore.class);
        meetingStore = mock(MeetingStore.class);
        meetingMemberStore = mock(MeetingMemberStore.class);
        userReader = mock(UserReader.class);
        imageStore = mock(ImageStore.class);
        imageAsyncStore = mock(ImageAsyncStore.class);

        crewService = new CrewServiceImpl(crewReader, crewStore, crewTagReader, crewTagStore, crewMemberReader, crewMemberStore, crewApplicationReader, crewApplicationStore, meetingStore, meetingMemberStore, userReader, imageStore, imageAsyncStore);
    }

    @DisplayName("[getCrewDtoById] 크루 아이디로 크루 정보를 가져온다.")
    @Test
    void should_GetCrewDtoById() {
        // given
        Long crewId = 1L;
        Crew crew = createCrew(crewId);
        List<String> tags = List.of("태그1", "태그2", "태그3");
        given(crewReader.findById(crewId))
                .willReturn(Optional.of(crew));
        given(crewTagReader.getTagsByCrewId(crewId))
                .willReturn(tags);

        // when
        crewService.getCrewDtoById(crewId);

        // then
        then(crewReader).should(only())
                .findById(anyLong());
        then(crewTagReader).should(only())
                .getTagsByCrewId(anyLong());
    }

    @DisplayName("[getCrewDtoById] 존재하는 크루 아이디인 경우, NotFound 예외를 발생시킨다.")
    @Test
    void when_NotExistingCrewId_Then_NotFoundException() {
        // given
        Long crewId = 1L;
        given(crewReader.findById(crewId))
                .willReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.getCrewDtoById(crewId));

        assertEquals(CrewErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
        then(crewReader).should(only())
                .findById(anyLong());
        then(crewTagReader).shouldHaveNoInteractions();
    }

    @DisplayName("[createCrew] 크루를 생성한다.")
    @Test
    void should_CreateCrew() {
        // given
        CrewDto crewDto = createCrewDto();
        MultipartFile file = mock(MultipartFile.class);
        int participatingCrewCount = 2;
        int applyingCrewCount = 1;
        given(crewMemberReader.countByUserId(anyLong()))
                .willReturn(participatingCrewCount);
        given(crewApplicationReader.countByUserId(anyLong()))
                .willReturn(applyingCrewCount);
        String icon = "크루아이콘";
        given(imageAsyncStore.upload(S3Folder.CREW, file))
                .willReturn(icon);
        Crew crew = createCrew(crewDto.id());
        given(crewStore.save(any()))
                .willReturn(crew);

        // when
        crewService.createCrew(crewDto, file);

        // then
        then(imageAsyncStore).should(only())
                .upload(any(S3Folder.class), any(MultipartFile.class));
        then(crewStore).should(only())
                .save(any());
        then(crewTagStore).should(only())
                .addTags(any(), anyList());
        then(crewMemberStore).should(only())
                .addCrewMember(anyLong(), anyLong());
    }

    @DisplayName("[createCrew] 크루 참여/신청 횟수가 5 이상이면, MaximumParticipation 예외를 발생시킨다.")
    @Test
    void when_OverMaximumParticipation_Then_MaximumParticipationException() {
        // given
        CrewDto crewDto = createCrewDto();
        MultipartFile file = mock(MultipartFile.class);
        int participatingCrewCount = 2;
        int applyingCrewCount = 3;
        given(crewMemberReader.countByUserId(anyLong()))
                .willReturn(participatingCrewCount);
        given(crewApplicationReader.countByUserId(anyLong()))
                .willReturn(applyingCrewCount);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.createCrew(crewDto, file));

        assertEquals(CrewErrorCode.MAXIMUM_PARTICIPATION.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .countByUserId(anyLong());
        then(crewApplicationReader).should(only())
                .countByUserId(anyLong());
        then(imageAsyncStore).shouldHaveNoInteractions();
        then(crewStore).shouldHaveNoInteractions();
        then(crewTagStore).shouldHaveNoInteractions();
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("[getCrewDtosByRank] 페이지와 랭킹에 따른 크루 목록을 가져온다.")
    @Test
    void should_GetCrewDtosByRank() {
        // given
        Pageable pageable = mock(Pageable.class);
        given(crewReader.getCrewsByRank(pageable))
                .willReturn(mock(PageImpl.class));

        // when
        crewService.getCrewDtosByRank(pageable);

        // then
        then(crewReader).should(only())
                .getCrewsByRank(any());
    }

    @DisplayName("[getCrewDtosByRecommend] 페이지와 추천에 따른 크루 목록을 가져온다.")
    @Test
    void should_GetCrewDtosByRecommend() {
        // given
        Pageable pageable = mock(Pageable.class);
        given(crewReader.getCrewsByRecommend(pageable))
                .willReturn(mock(PageImpl.class));

        // when
        crewService.getCrewDtosByRecommend(pageable);

        // then
        then(crewReader).should(only())
                .getCrewsByRecommend(any());
    }

    @DisplayName("[getCrewDtosByUserId] 페이지와 유저 아이디에 따른 크루 목록을 가져온다.")
    @Test
    void should_GetCrewDtosByUserId() {
        // given
        Long userId = 1L;
        Pageable pageable = mock(Pageable.class);
        given(crewReader.getCrewsByUserId(userId, pageable))
                .willReturn(mock(PageImpl.class));

        // when
        crewService.getCrewDtosByUserId(userId, pageable);

        // then
        then(crewReader).should(only())
                .getCrewsByUserId(anyLong(), any());
    }

    @DisplayName("[getUserImagesByCrew] 크루에 따른 유저이미지를 크루장을 가장 처음으로 설정하여 Maximum만큼 가져온다.")
    @Test
    void should_GetUserImagesByCrew() {
        // given
        CrewDto crewDto = createCrewDto();
        List<CrewMember> otherMembers = List.of(
                createCrewMember(crewDto.id(), 1L),
                createCrewMember(crewDto.id(), 2L),
                createCrewMember(crewDto.id(), 3L)
        );
        given(crewMemberReader.getOtherCrewMembersByCrewIdMaximum(anyLong(), anyLong(), anyInt()))
                .willReturn(otherMembers);
        given(userReader.getUserById(anyLong()))
                .willReturn(createUser(1L), createUser(2L), createUser(3L));

        // when
        List<String> userImages = crewService.getUserImagesByCrew(crewDto);

        // then
        assertEquals(1 + otherMembers.size(), userImages.size());
        assertEquals(crewDto.userDto().profileImage(), userImages.get(0));
        then(crewMemberReader).should(only())
                .getOtherCrewMembersByCrewIdMaximum(anyLong(), anyLong(), anyInt());
        then(userReader).should(times(otherMembers.size()))
                .getUserById(anyLong());
    }

    @DisplayName("[existCrewMember] 크루원의 존재여부를 가져온다.")
    @Test
    void should_ExistCrewMember() {
        // given
        CrewMemberPK crewMemberPK = CrewMemberPK.of(1L, 1L);
        given(crewMemberReader.existsByCrewMemberPK(crewMemberPK))
                .willReturn(true);

        // when
        boolean isExist = crewService.existCrewMember(crewMemberPK);

        // then
        assertTrue(isExist);
        then(crewMemberReader).should(only())
                .existsByCrewMemberPK(any());
    }

    @DisplayName("[updateCrewInfo] 크루 정보를 업데이트한다.")
    @Test
    void should_UpdateCrewInfo() {
    }

    @DisplayName("[updateCrewImage] 크루 이미지를 업데이트한다.")
    @Test
    void should_UpdateCrewImage() {
        // given
        CrewDto crewDto = createCrewDto();
        MultipartFile file = mock(MultipartFile.class);
        Long userId = crewDto.userDto().id();
        String newImage = "크루아이콘";
        given(imageAsyncStore.upload(S3Folder.CREW, file))
                .willReturn(newImage);

        // when
        crewService.updateCrewImage(crewDto, file, userId);

        // then
        then(imageAsyncStore).should(only())
                .upload(any(), any(MultipartFile.class));
        then(crewStore).should(only())
                .save(any());
        then(imageStore).should(only())
                .delete(any(), anyString());
    }

    @DisplayName("[updateCrewImage] 크루장이 아닌 경우, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException1() {
        // given
        CrewDto crewDto = createCrewDto();
        MultipartFile file = mock(MultipartFile.class);
        Long userId = 2134L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.updateCrewImage(crewDto, file, userId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
        then(imageAsyncStore).shouldHaveNoInteractions();
        then(crewStore).shouldHaveNoInteractions();
        then(imageStore).shouldHaveNoInteractions();
    }

    @DisplayName("[deleteCrew] 크루를 삭제한다.")
    @Test
    void should_DeleteCrew() {
        // given
        CrewDto crewDto = createCrewDto();
        Long userId = crewDto.userDto().id();

        // when
        crewService.deleteCrew(crewDto, userId);

        // then
        then(crewStore).should(only())
                .delete(any());
        then(imageStore).should(only())
                .delete(any(), anyString());
    }

    @DisplayName("[deleteCrew] 크루장이 아닌 경우, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException2() {
        // given
        CrewDto crewDto = createCrewDto();
        Long userId = 284L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.deleteCrew(crewDto, userId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
        then(crewStore).shouldHaveNoInteractions();
        then(imageStore).shouldHaveNoInteractions();
    }

    @DisplayName("[applyCrew] 크루에 가입신청을 한다.")
    @Test
    void should_ApplyCrew() {
    }

    @DisplayName("[withdrawApplication] 크루 가입신청을 취소한다.")
    @Test
    void should_WithdrawApplication() {
        // given
        Long userId = 1L;
        CrewDto crewDto = createCrewDto();
        given(crewApplicationReader.existsByCrewIdAndUserId(crewDto.id(), userId))
                .willReturn(true);

        // when
        crewService.withdrawApplication(crewDto, userId);

        // then
        then(crewApplicationReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewApplicationStore).should(only())
                .delete(any(), any());
    }

    @DisplayName("[withdrawApplication] 존재하지 않는 신청의 경우, NotFoundApplication 예외를 발생시킨다.")
    @Test
    void when_NotExistingApplication_Then_NotFoundApplicationException1() {
        // given
        Long userId = 1L;
        CrewDto crewDto = createCrewDto();
        given(crewApplicationReader.existsByCrewIdAndUserId(crewDto.id(), userId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.withdrawApplication(crewDto, userId));

        assertEquals(CrewErrorCode.NOT_FOUND_APPLICATION.getMessage(), exception.getMessage());
        then(crewApplicationReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewApplicationStore).shouldHaveNoInteractions();
    }

    @DisplayName("[approveApplication] 크루 가입신청을 승인한다.")
    @Test
    void should_ApproveApplication() {
        // given
        CrewDto crewDto = createCrewDto();
        Long applicantId = 2L;
        given(crewApplicationReader.existsByCrewIdAndUserId(crewDto.id(), applicantId))
                .willReturn(true);

        // when
        crewService.approveApplication(crewDto, applicantId, crewDto.userDto().id());

        // then
        then(crewApplicationReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewApplicationStore).should(only())
                .approve(anyLong(), anyLong());
    }

    @DisplayName("[approveApplication] 크루장이 아닌 경우, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException3() {
        // given
        CrewDto crewDto = createCrewDto();
        Long applicantId = 2L;
        Long invalidUserId = 3L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.approveApplication(crewDto, applicantId, invalidUserId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
        then(crewApplicationReader).shouldHaveNoInteractions();
        then(crewApplicationStore).shouldHaveNoInteractions();
    }

    @DisplayName("[approveApplication] 존재하지 않는 신청의 경우, NotFoundApplication 예외를 발생시킨다.")
    @Test
    void when_NotExistingApplication_Then_NotFoundApplicationException2() {
        // given
        CrewDto crewDto = createCrewDto();
        Long applicantId = 2L;
        given(crewApplicationReader.existsByCrewIdAndUserId(crewDto.id(), applicantId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.approveApplication(crewDto, applicantId, crewDto.userDto().id()));

        assertEquals(CrewErrorCode.NOT_FOUND_APPLICATION.getMessage(), exception.getMessage());
        then(crewApplicationReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewApplicationStore).shouldHaveNoInteractions();
    }

    @DisplayName("[disapproveApplication] 크루 가입신청을 거절한다.")
    @Test
    void should_DisapproveApplication() {
        // given
        CrewDto crewDto = createCrewDto();
        Long applicantId = 2L;
        given(crewApplicationReader.existsByCrewIdAndUserId(crewDto.id(), applicantId))
                .willReturn(true);

        // when
        crewService.disapproveApplication(crewDto, applicantId, crewDto.userDto().id());

        // then
        then(crewApplicationReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewApplicationStore).should(only())
                .delete(anyLong(), anyLong());
    }

    @DisplayName("[disapproveApplication] 크루장이 아닌 경우, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException4() {
        // given
        CrewDto crewDto = createCrewDto();
        Long applicantId = 2L;
        Long invalidUserId = 3L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.disapproveApplication(crewDto, applicantId, invalidUserId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
        then(crewApplicationReader).shouldHaveNoInteractions();
        then(crewApplicationStore).shouldHaveNoInteractions();
    }

    @DisplayName("[disapproveApplication] 존재하지 않는 신청의 경우, NotFoundApplication 예외를 발생시킨다.")
    @Test
    void when_NotExistingApplication_Then_NotFoundApplicationException3() {
        // given
        CrewDto crewDto = createCrewDto();
        Long applicantId = 2L;
        given(crewApplicationReader.existsByCrewIdAndUserId(crewDto.id(), applicantId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.disapproveApplication(crewDto, applicantId, crewDto.userDto().id()));

        assertEquals(CrewErrorCode.NOT_FOUND_APPLICATION.getMessage(), exception.getMessage());
        then(crewApplicationReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewApplicationStore).shouldHaveNoInteractions();
    }

    @DisplayName("[leaveCrew] 크루를 탈퇴한다.")
    @Test
    void should_LeaveCrew() {
        // given
        CrewDto crewDto = createCrewDto();
        Long userId = 11L;
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userId))
                .willReturn(true);

        // when
        crewService.leaveCrew(crewDto, userId);

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(only())
                .removeAllByUserIdAndCrewId(anyLong(), anyLong());
        then(meetingStore).should(only())
                .deleteAllByUserIdAndCrewId(anyLong(), anyLong());
        then(crewMemberStore).should(only())
                .subtractCrewMember(anyLong(), anyLong());
    }

    @DisplayName("[leaveCrew] 탈퇴하려는 크루원이 크루장일 경우, CreatorDeleteForbidden 예외를 발생시킨다.")
    @Test
    void when_CrewCreator_Then_CreatorDeleteForbiddenException() {
        // given
        CrewDto crewDto = createCrewDto();
        Long userId = crewDto.userDto().id();

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.leaveCrew(crewDto, userId));

        assertEquals(CrewErrorCode.CREATOR_DELETE_FORBIDDEN.getMessage(), exception.getMessage());
        then(crewMemberReader).shouldHaveNoInteractions();
        then(meetingMemberStore).shouldHaveNoInteractions();
        then(meetingStore).shouldHaveNoInteractions();
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("[leaveCrew] 존재하지 않는 크루원의 경우, NotCrewMember 예외를 발생시킨다.")
    @Test
    void when_NotExistingCrewMember_Then_NotFoundCrewMemberException() {
        // given
        CrewDto crewDto = createCrewDto();
        Long userId = 11L;
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.leaveCrew(crewDto, userId));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).shouldHaveNoInteractions();
        then(meetingStore).shouldHaveNoInteractions();
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("크루원을 추방한다.")
    @Test
    void should_ExpelMember() {
        // given
        CrewDto crewDto = createCrewDto();
        Long memberId = 11L;
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), memberId))
                .willReturn(true);

        // when
        crewService.expelMember(crewDto, memberId, crewDto.userDto().id());

        // then
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).should(only())
                .removeAllByUserIdAndCrewId(anyLong(), anyLong());
        then(meetingStore).should(only())
                .deleteAllByUserIdAndCrewId(anyLong(), anyLong());
        then(crewMemberStore).should(only())
                .subtractCrewMember(anyLong(), anyLong());
    }

    @DisplayName("크루원을 추방할 때, 크루장이 아닌 경우, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException5() {
        // given
        CrewDto crewDto = createCrewDto();
        Long memberId = 11L;
        Long invalidUserId = 12L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.expelMember(crewDto, memberId, invalidUserId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
        then(crewMemberReader).shouldHaveNoInteractions();
        then(meetingMemberStore).shouldHaveNoInteractions();
        then(meetingStore).shouldHaveNoInteractions();
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("추방하려는 크루원이 크루장일 경우, CreatorDeleteForbidden 예외를 발생시킨다.")
    @Test
    void when_CrewCreator_Then_CreatorExpelForbiddenException() {
        // given
        CrewDto crewDto = createCrewDto();
        Long userId = crewDto.userDto().id();

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.expelMember(crewDto, userId, userId));

        assertEquals(CrewErrorCode.CREATOR_DELETE_FORBIDDEN.getMessage(), exception.getMessage());
        then(crewMemberReader).shouldHaveNoInteractions();
        then(meetingMemberStore).shouldHaveNoInteractions();
        then(meetingStore).shouldHaveNoInteractions();
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("크루원을 추방할 때, 존재하지 않는 크루원의 경우, NotCrewMember 예외를 발생시킨다.")
    @Test
    void when_NotExistingCrewMember_Then_NotFoundCrewMemberException2() {
        // given
        CrewDto crewDto = createCrewDto();
        Long memberId = 11L;
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), memberId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewService.expelMember(crewDto, memberId, crewDto.userDto().id()));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(meetingMemberStore).shouldHaveNoInteractions();
        then(meetingStore).shouldHaveNoInteractions();
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    private User createUser() {
        return User.of(
                1525L,
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

    private CrewMember createCrewMember(Long crewId, Long userId) {
        return CrewMember.of(
                CrewMemberPK.of(userId, crewId),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Crew createCrew(Long crewId) {
        return Crew.of(
                crewId,
                "크루 이름",
                12,
                30,
                Region.강남구,
                "크루 설명",
                "크루 이미지",
                CrewStatus.ACTIVATED,
                createUser(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                17,
                45,
                CrewGender.ANY,
                true,
                null,
                false
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
}