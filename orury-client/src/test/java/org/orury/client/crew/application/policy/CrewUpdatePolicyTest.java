package org.orury.client.crew.application.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewApplicationReader;
import org.orury.domain.crew.domain.CrewApplicationStore;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.user.domain.UserReader;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.orury.domain.DomainFixtureFactory.TestCrewDto.createCrewDto;

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
}