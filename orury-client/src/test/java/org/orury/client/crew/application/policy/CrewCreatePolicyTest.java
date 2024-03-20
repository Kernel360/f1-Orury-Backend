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
import org.orury.domain.crew.domain.dto.CrewDto;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.orury.domain.DomainFixtureFactory.TestCrewDto.createCrewDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Policy] 크루 생성 Policy 테스트")
@ActiveProfiles("test")
class CrewCreatePolicyTest {
    private CrewCreatePolicy crewCreatePolicy;
    private CrewMemberReader crewMemberReader;
    private CrewApplicationReader crewApplicationReader;

    @BeforeEach
    void setUp() {
        crewMemberReader = mock(CrewMemberReader.class);
        crewApplicationReader = mock(CrewApplicationReader.class);

        crewCreatePolicy = new CrewCreatePolicy(crewMemberReader, crewApplicationReader);
    }

    @DisplayName("[createCrew] 크루 참여/신청 횟수가 5 이상이면, MaximumParticipation 예외를 발생시킨다.")
    @Test
    void when_OverMaximumParticipation_Then_MaximumParticipationException() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        given(crewMemberReader.countByUserId(anyLong())).willReturn(3);
        given(crewApplicationReader.countByUserId(anyLong())).willReturn(2);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> crewCreatePolicy.validate(crewDto));

        then(crewMemberReader).should(only())
                .countByUserId(anyLong());
        then(crewApplicationReader).should(only())
                .countByUserId(anyLong());
        assertEquals(CrewErrorCode.MAXIMUM_PARTICIPATION.getMessage(), exception.getMessage());
    }
}