package org.orury.domain.crew.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.user.domain.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.orury.domain.CrewDomainFixture.TestCrewMember.createCrewMember;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Reader] 크루멤버 ReaderImpl 테스트")
@ActiveProfiles("test")
class CrewMemberReaderImplTest extends InfrastructureTest {

    @DisplayName("크루id와 멤버id를 받아, 크루멤버를 조회한다.")
    @Test
    void findCrewMemberByCrewIdAndMemberId() {
        // given & when
        crewMemberReader.existsByCrewIdAndUserId(anyLong(), anyLong());

        // then
        then(crewMemberRepository).should(only())
                .existsByCrewMemberPK_CrewIdAndCrewMemberPK_UserId(anyLong(), anyLong());
    }

    @DisplayName("유저id를 받아, 해당 유저의 크루멤버 수를 조회한다.")
    @Test
    void countCrewMembersByUserId() {
        // given & when
        crewMemberReader.countByUserId(anyLong());

        // then
        then(crewMemberRepository).should(only())
                .countByCrewMemberPK_UserId(anyLong());
    }

    @DisplayName("크루id를 받아, 해당 크루의 전체멤버의 유저 목록을 조회한다.")
    @Test
    void getMembersByCrewId() {
        // given & when
        List<CrewMember> crewMembers = List.of(
                createCrewMember().build().get(),
                createCrewMember().build().get(),
                createCrewMember().build().get()
        );
        given(crewMemberRepository.findByCrewMemberPK_CrewId(anyLong()))
                .willReturn(crewMembers);
        given(userRepository.findUserById(anyLong()))
                .willReturn(mock(User.class));

        crewMemberReader.getMembersByCrewId(anyLong());

        // then
        then(crewMemberRepository).should(only())
                .findByCrewMemberPK_CrewId(anyLong());
        then(userRepository).should(times(crewMembers.size()))
                .findUserById(anyLong());
    }

    @DisplayName("크루id와 크루 생성자id, 최대 조회수를 받아, 해당 크루의 멤버 목록을 조회한다.")
    @Test
    void getOtherCrewMembersByCrewIdMaximum() {
        // given & when
        crewMemberReader.getOtherCrewMembersByCrewIdMaximum(anyLong(), anyLong(), Math.abs(anyInt()) + 1);

        // then
        then(crewMemberRepository).should(only())
                .findByCrewMemberPK_CrewIdAndCrewMemberPK_UserIdNot(anyLong(), anyLong(), any(PageRequest.class));
    }
}
