package org.orury.client.crew.application.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewApplicationReader;
import org.orury.domain.crew.domain.CrewApplicationStore;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.orury.domain.CrewDomainFixture.TestCrewApplication.createCrewApplication;
import static org.orury.domain.CrewDomainFixture.TestCrewDto.createCrewDto;
import static org.orury.domain.UserDomainFixture.TestUser.createUser;
import static org.orury.domain.UserDomainFixture.TestUserDto.createUserDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Policy] 크루 수정 Policy 테스트")
@ActiveProfiles("test")
class CrewUpdatePolicyTest {
    private CrewUpdatePolicy crewUpdatePolicy;
    private CrewMemberReader crewMemberReader;
    private CrewApplicationReader crewApplicationReader;
    private UserReader userReader;
    private CrewApplicationStore crewApplicationStore;

    @BeforeEach
    void setUp() {
        crewMemberReader = mock(CrewMemberReader.class);
        crewApplicationReader = mock(CrewApplicationReader.class);
        userReader = mock(UserReader.class);
        crewApplicationStore = mock(CrewApplicationStore.class);

        crewUpdatePolicy = new CrewUpdatePolicy(crewMemberReader, crewApplicationReader, userReader, crewApplicationStore);
    }

    @DisplayName("[updateCrewImage] 크루장이 아닌 경우, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException1() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        Long userId = 2134L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewUpdatePolicy.validateCrewCreator(crewDto.userDto().id(), userId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
    }

    @DisplayName("[deleteCrew] 크루장이 아닌 경우, Forbidden 예외를 발생시킨다.")
    @Test
    void when_NotCrewCreator_Then_ForbiddenException2() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        Long userId = 284L;

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewUpdatePolicy.validateCrewCreator(crewDto.userDto().id(), userId));

        assertEquals(CrewErrorCode.FORBIDDEN.getMessage(), exception.getMessage());
    }

    @DisplayName("[updateCrewInfo] 크루 정보 업데이트 유효성 검사를 통과한다.")
    @Test
    void when_UpdateCrewInfo_Then_UpdateSuccessfully() {
        // given
        CrewDto oldCrew = createCrewDto().build().get();
        CrewDto newCrew = createCrewDto().name("newCrew").build().get();
        UserDto userDto = createUserDto(oldCrew.userDto().id()).build().get();

        CrewUpdatePolicy spyCrewUpdatePolicy = Mockito.spy(crewUpdatePolicy);

        // when
        spyCrewUpdatePolicy.validateUpdateCrewInfo(oldCrew, newCrew, userDto.id());

        // then
        then(spyCrewUpdatePolicy).should()
                .validateCrewCreator(oldCrew.userDto().id(), userDto.id());
    }

    @DisplayName("[updateCrewInfo] 현재 크루 인원보다 전체 인원이 적은 경우, MEMBER_OVERFLOW 예외를 발생시킨다.")
    @Test
    void when_CapacityIsLessThanMemberCount_Then_MemberOverflowException() {
        // given
        CrewDto oldCrew = createCrewDto().capacity(13).memberCount(11).build().get();
        CrewDto newCrew = createCrewDto().capacity(10).memberCount(11).build().get();

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewUpdatePolicy.validateUpdateCrewInfo(oldCrew, newCrew, oldCrew.userDto().id()));

        assertEquals(CrewErrorCode.MEMBER_OVERFLOW.getMessage(), exception.getMessage());
    }

    @DisplayName("[updateCrewInfo] 현재 크루원들의 연령이 새로운 크루의 연령기준에 맞지 않는 경우, AGE_FORBIDDEN 예외를 발생시킨다.")
    @Test
    void when_MembersAgeIsNotMatched_Then_AgeForbiddenException() {
        // given
        CrewDto oldCrew = createCrewDto().minAge(20).maxAge(30).build().get();
        CrewDto newCrew = createCrewDto().minAge(25).maxAge(30).build().get();
        List<User> members = List.of(
                // 20살과 새로운 크루의 연령기준에 맞지 않음
                createUserDto().birthday(LocalDate.now().minusYears(20)).build().get().toEntity(),
                createUserDto().birthday(LocalDate.now().minusYears(25)).build().get().toEntity()
        );
        given(crewMemberReader.getMembersByCrewId(oldCrew.id())).willReturn(members);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewUpdatePolicy.validateUpdateCrewInfo(oldCrew, newCrew, oldCrew.userDto().id()));

        assertEquals(CrewErrorCode.AGE_FORBIDDEN.getMessage(), exception.getMessage());
    }

    @DisplayName("[updateCrewInfo] 현재 크루원들의 성별이 새로운 크루의 성별기준에 맞지 않는 경우, GENDER_FORBIDDEN 예외를 발생시킨다.")
    @Test
    void when_MembersGenderIsNotMatched_Then_GenderForbiddenException() {
        // given
        CrewDto oldCrew = createCrewDto().gender(CrewGender.ANY).build().get();
        CrewDto newCrew = createCrewDto().gender(CrewGender.FEMALE).build().get();
        List<User> members = List.of(
                // 여성과 새로운 크루의 성별기준에 맞지 않음
                createUserDto().gender(NumberConstants.FEMALE).build().get().toEntity(),
                createUserDto().gender(NumberConstants.MALE).build().get().toEntity()
        );
        given(crewMemberReader.getMembersByCrewId(oldCrew.id())).willReturn(members);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewUpdatePolicy.validateUpdateCrewInfo(oldCrew, newCrew, oldCrew.userDto().id()));

        assertEquals(CrewErrorCode.GENDER_FORBIDDEN.getMessage(), exception.getMessage());
    }

    @DisplayName("[updateCrewInfo] 크루가 승인 후 가입에서 바로가입으로 변경될 때, 조건에 맞는 크루 가입신청유저들을 모두 가입처리한다.")
    @Test
    void when_PermissionRequiredIsFalse_Then_ApproveAllApplications() {
        // given
        CrewDto oldCrew = createCrewDto()
                .gender(CrewGender.ANY)
                .minAge(20)
                .maxAge(30)
                .permissionRequired(true).build().get();
        CrewDto newCrew = createCrewDto()
                .gender(CrewGender.MALE)
                .minAge(25)
                .maxAge(30)
                .permissionRequired(false).build().get();
        List<User> applicants = List.of(
                // 조건에 맞는 유저
                createUser()
                        .id(1L)
                        .gender(NumberConstants.MALE)
                        .birthday(LocalDate.now().minusYears(25))
                        .build().get(),
                createUser()
                        .id(2L)
                        .gender(NumberConstants.MALE)
                        .birthday(LocalDate.now().minusYears(27))
                        .build().get(),

                // 조건에 맞지 않는 유저
                createUser()
                        .id(3L)
                        .gender(NumberConstants.FEMALE)
                        .birthday(LocalDate.now().minusYears(25))
                        .build().get(),
                createUser()
                        .id(4L)
                        .gender(NumberConstants.MALE)
                        .birthday(LocalDate.now().minusYears(20))
                        .build().get()
        );
        List<CrewApplication> applications = List.of(
                createCrewApplication(oldCrew.id(), applicants.get(0).getId()).build().get(),
                createCrewApplication(oldCrew.id(), applicants.get(1).getId()).build().get(),
                createCrewApplication(oldCrew.id(), applicants.get(2).getId()).build().get(),
                createCrewApplication(oldCrew.id(), applicants.get(3).getId()).build().get()
        );
        given(crewApplicationReader.findAllByCrewId(oldCrew.id())).willReturn(applications);
        given(userReader.getUserById(anyLong())).willReturn(
                applicants.get(0), applicants.get(1), applicants.get(2), applicants.get(3));

        // when
        crewUpdatePolicy.validateUpdateCrewInfo(oldCrew, newCrew, oldCrew.userDto().id());

        // then
        then(crewApplicationStore).should(times(2)).approve(any());
        then(crewApplicationStore).should(times(2)).delete(any());
    }

    @DisplayName("[updateCrewInfo] 크루가 승인 후 가입에서 바로가입으로 변경될 때, 크루원수 + 유효한 지원자수가 변경된 크루의 정원보다 많은 경우, APPLICATION_OVERFLOW 예외를 발생시킨다.")
    @Test
    void when_CapacityIsLessThanMemberCountPlusValidApplications_Then_ApplicationOverflowException() {
        // given
        CrewDto oldCrew = createCrewDto()
                .gender(CrewGender.ANY)
                .capacity(5)
                .memberCount(5)
                .permissionRequired(true).build().get();
        CrewDto newCrew = createCrewDto()
                .gender(CrewGender.ANY)
                .capacity(8)
                .memberCount(5)
                .permissionRequired(false).build().get();
        List<User> applicants = List.of(
                createUser()
                        .id(1L)
                        .gender(NumberConstants.MALE)
                        .birthday(LocalDate.now().minusYears(25))
                        .build().get(),
                createUser()
                        .id(2L)
                        .gender(NumberConstants.MALE)
                        .birthday(LocalDate.now().minusYears(26))
                        .build().get(),
                createUser()
                        .id(3L)
                        .gender(NumberConstants.FEMALE)
                        .birthday(LocalDate.now().minusYears(24))
                        .build().get(),
                createUser()
                        .id(4L)
                        .gender(NumberConstants.MALE)
                        .birthday(LocalDate.now().minusYears(27))
                        .build().get()
        );
        List<CrewApplication> applications = List.of(
                createCrewApplication(oldCrew.id(), applicants.get(0).getId()).build().get(),
                createCrewApplication(oldCrew.id(), applicants.get(1).getId()).build().get(),
                createCrewApplication(oldCrew.id(), applicants.get(2).getId()).build().get(),
                createCrewApplication(oldCrew.id(), applicants.get(3).getId()).build().get()
        );
        given(crewApplicationReader.findAllByCrewId(oldCrew.id())).willReturn(applications);
        given(userReader.getUserById(anyLong())).willReturn(
                applicants.get(0), applicants.get(1), applicants.get(2), applicants.get(3));

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewUpdatePolicy.validateUpdateCrewInfo(oldCrew, newCrew, oldCrew.userDto().id()));

        assertEquals(CrewErrorCode.APPLICATION_OVERFLOW.getMessage(), exception.getMessage());
    }
}