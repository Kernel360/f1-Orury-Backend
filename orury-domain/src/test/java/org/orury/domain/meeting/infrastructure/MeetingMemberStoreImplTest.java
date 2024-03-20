package org.orury.domain.meeting.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.meeting.domain.entity.MeetingMemberPK;
import org.orury.domain.user.domain.entity.User;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Store] 일정멤버 StoreImpl 테스트")
@ActiveProfiles("test")
class MeetingMemberStoreImplTest {
    private MeetingMemberStore meetingMemberStore;
    private MeetingMemberRepository meetingMemberRepository;
    private MeetingRepository meetingRepository;

    @BeforeEach
    void setUp() {
        meetingMemberRepository = mock(MeetingMemberRepository.class);
        meetingRepository = mock(MeetingRepository.class);

        meetingMemberStore = new MeetingMemberStoreImpl(meetingMemberRepository, meetingRepository);
    }

    @DisplayName("일정멤버를 받아, 일정멤버를 저장하고, 일정의 memberCount를 증가시킨다.")
    @Test
    void addMember() {
        // given
        MeetingMember meetingMember = mock(MeetingMember.class);
        given(meetingMember.getMeetingMemberPK())
                .willReturn(mock(MeetingMemberPK.class));

        // when
        meetingMemberStore.addMember(meetingMember);

        // then
        then(meetingMemberRepository).should(only())
                .save(any());
        then(meetingRepository).should(only())
                .increaseMemberCount(anyLong());
    }

    @DisplayName("일정멤버를 받아, 일정멤버를 삭제하고, 일정의 memberCount를 감소시킨다.")
    @Test
    void removeMember() {
        // given
        MeetingMember meetingMember = mock(MeetingMember.class);
        given(meetingMember.getMeetingMemberPK())
                .willReturn(mock(MeetingMemberPK.class));

        // when
        meetingMemberStore.removeMember(meetingMember);

        // then
        then(meetingMemberRepository).should(only())
                .delete(any());
        then(meetingRepository).should(only())
                .decreaseMemberCount(anyLong());
    }

    @DisplayName("유저id와 크루id를 받아, 크루에 해당 유저가 참여한 일정에서 유저를 삭제하고, 일정의 memberCount를 감소시킨다.")
    @Test
    void removeAllByUserIdAndCrewId() {
        // given & when
        List<Meeting> meetings = List.of(createMeeting(1L), createMeeting(2L), createMeeting(3L));
        given(meetingRepository.findAllByCrew_Id(anyLong()))
                .willReturn(meetings);
        given(meetingMemberRepository.findByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserId(anyLong(), anyLong()))
                .willReturn(
                        Optional.of(createMeetingMember(1L)),
                        Optional.empty(),
                        Optional.of(createMeetingMember(3L))
                );

        meetingMemberStore.removeAllByUserIdAndCrewId(
                mock(User.class).getId(),
                mock(Crew.class).getId()
        );

        // then
        then(meetingRepository).should(times(1))
                .findAllByCrew_Id(anyLong());
        then(meetingMemberRepository).should(times(2))
                .delete(any(MeetingMember.class));
        then(meetingRepository).should(times(2))
                .decreaseMemberCount(anyLong());
    }

    private Meeting createMeeting(Long meetingId) {
        return Meeting.of(
                meetingId,
                LocalDateTime.of(2222, 3, 14, 18, 32),
                1,
                5,
                mock(User.class),
                mock(Gym.class),
                mock(Crew.class),
                LocalDateTime.of(2023, 12, 9, 7, 30),
                LocalDateTime.of(2024, 3, 14, 18, 32)
        );
    }

    private MeetingMember createMeetingMember(Long userId) {
        MeetingMemberPK meetingMemberPK = MeetingMemberPK.of(userId, 2424L);
        return MeetingMember.of(meetingMemberPK);
    }
}