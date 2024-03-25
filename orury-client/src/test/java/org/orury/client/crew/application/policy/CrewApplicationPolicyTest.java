package org.orury.client.crew.application.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewApplicationReader;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.CrewMemberStore;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.orury.domain.CrewDomainFixture.TestCrewDto.createCrewDto;
import static org.orury.domain.UserDomainFixture.TestUserDto.createUserDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Policy] 크루 신청 Policy 테스트")
@ActiveProfiles("test")
class CrewApplicationPolicyTest {
    private CrewApplicationPolicy crewApplicationPolicy;
    private CrewApplicationReader crewApplicationReader;
    private CrewMemberReader crewMemberReader;
    private CrewMemberStore crewMemberStore;

    @BeforeEach
    void setUp() {
        crewApplicationReader = mock(CrewApplicationReader.class);
        crewMemberReader = mock(CrewMemberReader.class);
        crewMemberStore = mock(CrewMemberStore.class);

        crewApplicationPolicy = new CrewApplicationPolicy(crewApplicationReader, crewMemberReader, crewMemberStore);
    }

    @DisplayName("[applyCrew] 크루원이 이미 존재하는 경우, AlreadyMember 예외를 발생시킨다.")
    @Test
    void when_AlreadyMember_Then_AlreadyMemberException() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        UserDto userDto = createUserDto().build().get();
        String answer = "가입신청 답변";
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
                .willReturn(true);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateApplyCrew(crewDto, userDto, answer));

        assertEquals(CrewErrorCode.ALREADY_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewMemberReader).should(never())
                .countByUserId(anyLong());
        then(crewApplicationReader).shouldHaveNoInteractions();
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("[applyCrew] 크루 참여/신청 횟수가 5 이상이면, MaximumParticipation 예외를 발생시킨다.")
    @Test
    void when_OverMaximumParticipation_Then_MaximumParticipationException1() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        UserDto userDto = createUserDto().build().get();
        String answer = "가입신청 답변";
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
                .willReturn(false);
        given(crewMemberReader.countByUserId(userDto.id()))
                .willReturn(2);
        given(crewApplicationReader.countByUserId(userDto.id()))
                .willReturn(3);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateApplyCrew(crewDto, userDto, answer));

        assertEquals(CrewErrorCode.MAXIMUM_PARTICIPATION.getMessage(), exception.getMessage());
        then(crewMemberReader).should(times(1))
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewMemberReader).should(times(1))
                .countByUserId(anyLong());
        then(crewApplicationReader).should(only())
                .countByUserId(anyLong());
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("[applyCrew] 지원자의 연령이 크루 연령기준에 부합하지 않으면, AgeForbidden 예외를 발생시킨다.")
    @Test
    void when_AgeForbidden_Then_AgeForbiddenException() {
        // given
        CrewDto crewDto = createCrewDto()
                .id(23L)
                .minAge(15)
                .maxAge(30).build().get();
        UserDto userDto = createUserDto()
                .birthday(LocalDate.now().minusYears(14)).build().get();
        String answer = "가입신청 답변";
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
                .willReturn(false);
        given(crewMemberReader.countByUserId(userDto.id()))
                .willReturn(2);
        given(crewApplicationReader.countByUserId(userDto.id()))
                .willReturn(2);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateApplyCrew(crewDto, userDto, answer));

        assertEquals(CrewErrorCode.AGE_FORBIDDEN.getMessage(), exception.getMessage());
        then(crewMemberReader).should(times(1))
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewMemberReader).should(times(1))
                .countByUserId(anyLong());
        then(crewApplicationReader).should(times(1))
                .countByUserId(anyLong());
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("[applyCrew] 크루원의 성별이 크루 성별기준에 부합하지 않으면, GenderForbidden 예외를 발생시킨다.")
    @Test
    void when_GenderForbidden_Then_GenderForbiddenException() {
        // given
        CrewDto crewDto = createCrewDto()
                .gender(CrewGender.FEMALE).build().get();
        UserDto userDto = createUserDto()
                .gender(NumberConstants.MALE).build().get();
        String answer = "가입신청 답변";
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
                .willReturn(false);
        given(crewMemberReader.countByUserId(userDto.id()))
                .willReturn(2);
        given(crewApplicationReader.countByUserId(userDto.id()))
                .willReturn(2);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateApplyCrew(crewDto, userDto, answer));

        assertEquals(CrewErrorCode.GENDER_FORBIDDEN.getMessage(), exception.getMessage());
        then(crewMemberReader).should(times(1))
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewMemberReader).should(times(1))
                .countByUserId(anyLong());
        then(crewApplicationReader).should(times(1))
                .countByUserId(anyLong());
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("[applyCrew] 지원하는 크루가 즉시 가입이고 신청자가 가입조건을 만족하는 경우, 신청자를 바로 가입시킨다.")
    @Test
    void when_PermissionNotRequiredCrew_Then_ImmediateJoin() {
        // given
        CrewDto crewDto = createCrewDto()
                .minAge(25)
                .maxAge(30)
                .gender(CrewGender.FEMALE)
                .permissionRequired(false)
                .answerRequired(false).build().get();
        UserDto userDto = createUserDto()
                .birthday(LocalDate.now().minusYears(26))
                .gender(NumberConstants.FEMALE).build().get();

        String answer = "가입신청 답변";
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
                .willReturn(false);
        given(crewMemberReader.countByUserId(userDto.id()))
                .willReturn(2);
        given(crewApplicationReader.countByUserId(userDto.id()))
                .willReturn(2);

        // when
        crewApplicationPolicy.validateApplyCrew(crewDto, userDto, answer);

        // then
        then(crewMemberReader).should(times(1))
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewMemberReader).should(times(1))
                .countByUserId(anyLong());
        then(crewApplicationReader).should(times(1))
                .countByUserId(anyLong());
        then(crewMemberStore).should(times(1))
                .addCrewMember(anyLong(), anyLong());
    }

    @DisplayName("[applyCrew] 크루원의 가입신청 답변이 필수한데 답변이 없는 경우, EmptyAnswer 예외를 발생시킨다.")
    @Test
    void when_AnswerRequiredButNoAnswer_Then_AnswerRequiredException() {
        // given
        CrewDto crewDto = createCrewDto()
                .gender(CrewGender.ANY)
                .permissionRequired(true)
                .answerRequired(true).build().get();
        UserDto userDto = createUserDto().build().get();
        String answer = "";
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userDto.id()))
                .willReturn(false);
        given(crewMemberReader.countByUserId(userDto.id()))
                .willReturn(2);
        given(crewApplicationReader.countByUserId(userDto.id()))
                .willReturn(2);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateApplyCrew(crewDto, userDto, answer));

        assertEquals(CrewErrorCode.EMPTY_ANSWER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(times(1))
                .existsByCrewIdAndUserId(anyLong(), anyLong());
        then(crewMemberReader).should(times(1))
                .countByUserId(anyLong());
        then(crewApplicationReader).should(only())
                .countByUserId(anyLong());
        then(crewMemberStore).shouldHaveNoInteractions();
    }

    @DisplayName("존재하지 않는 신청의 경우, NotFoundApplication 예외를 발생시킨다.")
    @Test
    void when_NotExistingApplication_Then_NotFoundApplicationException1() {
        // given
        Long userId = 1L;
        CrewDto crewDto = createCrewDto().build().get();
        given(crewApplicationReader.existsByCrewIdAndUserId(crewDto.id(), userId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateApplication(crewDto.id(), userId));

        assertEquals(CrewErrorCode.NOT_FOUND_APPLICATION.getMessage(), exception.getMessage());
        then(crewApplicationReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
    }

    @DisplayName("크루장이 아닌 사람이 크루장의 권한이 필요한 작업 수행 시, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException3() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        Long invalidUserId = 3L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateCrewCreator(crewDto.userDto().id(), invalidUserId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
    }

    @DisplayName("존재하지 않는 크루원에 interaction할 경우, NotCrewMember 예외를 발생시킨다.")
    @Test
    void when_NotExistingCrewMember_Then_NotFoundCrewMemberException() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        Long userId = 11L;
        given(crewMemberReader.existsByCrewIdAndUserId(crewDto.id(), userId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewApplicationPolicy.validateCrewMember(crewDto.id(), userId));

        assertEquals(CrewErrorCode.NOT_CREW_MEMBER.getMessage(), exception.getMessage());
        then(crewMemberReader).should(only())
                .existsByCrewIdAndUserId(anyLong(), anyLong());
    }
}