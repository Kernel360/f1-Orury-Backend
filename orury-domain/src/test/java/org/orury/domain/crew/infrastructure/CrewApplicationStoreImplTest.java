package org.orury.domain.crew.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.CrewApplication;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.orury.domain.CrewDomainFixture.TestCrewApplicationPK.createCrewApplicationPK;
import static org.orury.domain.CrewDomainFixture.TestCrewDto.createCrewDto;
import static org.orury.domain.UserDomainFixture.TestUserDto.createUserDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Store] 크루지원 StoreImpl 테스트")
@ActiveProfiles("test")
class CrewApplicationStoreImplTest extends InfrastructureTest {

    @DisplayName("크루dto, 유저dto, 답변을 받아, 크루지원을 저장한다.")
    @Test
    void save() {
        // given
        CrewDto crewDto = createCrewDto().build().get();
        UserDto userDto = createUserDto().build().get();
        String answer = "answer";

        // when
        crewApplicationStore.save(crewDto, userDto, answer);

        // then
        then(crewApplicationRepository).should(only())
                .save(any(CrewApplication.class));
    }

    @DisplayName("크루id와 유저id를 받아, 크루지원을 승인한다.")
    @Test
    void approveByCrewIdAndUserId() {
        // given
        long crewId = 250L;
        long userId = 12489L;

        // when
        crewApplicationStore.approve(crewId, userId);

        // then
        then(crewApplicationRepository).should(only())
                .deleteById(any(CrewApplicationPK.class));
        then(crewMemberRepository).should(only())
                .save(any(CrewMember.class));
        then(crewRepository).should(only())
                .increaseMemberCount(anyLong());
    }


    @DisplayName("크루지원pk를 받아, 크루지원을 승인한다.")
    @Test
    void approveByCrewApplicationPK() {
        // given
        CrewApplicationPK crewApplicationPK = createCrewApplicationPK().build().get();

        // when
        crewApplicationStore.approve(crewApplicationPK);

        // then
        then(crewApplicationRepository).should(only())
                .deleteById(crewApplicationPK);
        then(crewMemberRepository).should(only())
                .save(any());
        then(crewRepository).should(only())
                .increaseMemberCount(crewApplicationPK.getCrewId());
    }

    @DisplayName("크루id와 유저id를 받아, 크루지원을 삭제한다.")
    @Test
    void deleteByCrewIdAndUserId() {
        // given
        long crewId = 250L;
        long userId = 12489L;

        // when
        crewApplicationStore.delete(crewId, userId);

        // then
        then(crewApplicationRepository).should(only())
                .deleteById(any(CrewApplicationPK.class));
    }

    @DisplayName("크루지원pk를 받아, 크루지원을 삭제한다.")
    @Test
    void deleteByCrewApplicationPK() {
        // given & when
        crewApplicationStore.delete(mock(CrewApplicationPK.class));

        // then
        then(crewApplicationRepository).should(only())
                .deleteById(any(CrewApplicationPK.class));
    }
}
