package org.orury.domain.crew.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.orury.domain.CrewDomainFixture.TestCrewMember.createCrewMember;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Reader] 크루 ReaderImpl 테스트")
@ActiveProfiles("test")
class CrewReaderImplTest extends InfrastructureTest {

    @DisplayName("크루id를 받아, 크루를 조회한다")
    @Test
    void findById() {
        // given & when
        crewReader.findById(anyLong());

        // then
        then(crewRepository).should(only())
                .findById(anyLong());
    }

    @DisplayName("Pageable을 받아, 멤버수 내림차순의 크루 목록을 조회한다")
    @Test
    void getCrewsByRank() {
        // given & when
        crewReader.getCrewsByRank(mock(Pageable.class));

        // then
        then(crewRepository).should(only())
                .findByOrderByMemberCountDesc(any(Pageable.class));
    }

    @DisplayName("Pageable을 받아, 생성일자 최신순의 크루 목록을 조회한다")
    @Test
    void getCrewsByRecommend() {
        // given & when
        crewReader.getCrewsByRecommend(mock(Pageable.class));

        // then
        then(crewRepository).should(only())
                .findByOrderByCreatedAtDesc(any(Pageable.class));
    }

    @DisplayName("userId와 Pageable을 받아, userId에 해당하는 크루 목록을 조회한다")
    @Test
    void getCrewsByUserId() {
        // given & when
        List<CrewMember> crewMembers = List.of(
                createCrewMember().build().get(),
                createCrewMember().build().get(),
                createCrewMember().build().get()
        );
        given(crewMemberRepository.findByCrewMemberPK_UserId(anyLong()))
                .willReturn(crewMembers);

        crewReader.getCrewsByUserId(4127L, mock(Pageable.class));

        // then
        then(crewMemberRepository).should(only())
                .findByCrewMemberPK_UserId(anyLong());
        then(crewRepository).should(only())
                .findAllByIdIn(anyList(), any(Pageable.class));
    }
}
