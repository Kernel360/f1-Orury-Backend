package org.orury.domain.crew.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Store] 크루멤버 StoreImpl 테스트")
@ActiveProfiles("test")
class CrewMemberStoreImplTest extends InfrastructureTest {

    @DisplayName("크루id와 멤버id를 받아, 크루멤버를 저장한다")
    @Test
    void addCrewMember() {
        // given & when
        crewMemberStore.addCrewMember(2578L, 204L);

        // then
        then(crewMemberRepository).should(only())
                .save(any(CrewMember.class));
        then(crewRepository).should(only())
                .increaseMemberCount(anyLong());
    }

    @DisplayName("크루id와 멤버id를 받아, 크루멤버를 제거한다")
    @Test
    void subtractCrewMember() {
        // given & when
        crewMemberStore.subtractCrewMember(2578L, 204L);

        // then
        then(crewMemberRepository).should(only())
                .deleteById(any(CrewMemberPK.class));
        then(crewRepository).should(only())
                .decreaseMemberCount(anyLong());
    }
}
