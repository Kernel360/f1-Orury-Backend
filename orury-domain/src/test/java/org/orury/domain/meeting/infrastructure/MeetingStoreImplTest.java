package org.orury.domain.meeting.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.meeting.domain.MeetingStore;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.user.domain.entity.User;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Store] 일정 StoreImpl 테스트")
@ActiveProfiles("test")
class MeetingStoreImplTest {
    private MeetingStore meetingStore;
    private MeetingRepository meetingRepository;

    @BeforeEach
    void setUp() {
        meetingRepository = mock(MeetingRepository.class);

        meetingStore = new MeetingStoreImpl(meetingRepository);
    }

    @DisplayName("일정을 받아, 일정을 저장한다.")
    @Test
    void createMeeting() {
        // given && when
        meetingStore.createMeeting(
                mock(Meeting.class)
        );

        // then
        then(meetingRepository).should(only())
                .save(any(Meeting.class));
    }

    @DisplayName("일정을 받아, 일정을 저장한다.")
    @Test
    void updateMeeting() {
        // given && when
        meetingStore.updateMeeting(
                mock(Meeting.class)
        );

        // then
        then(meetingRepository).should(only())
                .save(any(Meeting.class));
    }

    @DisplayName("일정id를 받아, 일정을 삭제한다.")
    @Test
    void deleteMeeting() {
        // given && when
        meetingStore.deleteMeeting(
                mock(Meeting.class).getId()
        );

        // then
        then(meetingRepository).should(only())
                .deleteById(anyLong());
    }

    @DisplayName("유저id와 일정id를 받아, 유저가 주최한 일정을 모두 삭제한다.")
    @Test
    void deleteAllByUserIdAndCrewId() {
        // given && when
        meetingStore.deleteAllByUserIdAndCrewId(
                mock(User.class).getId(),
                mock(Crew.class).getId()
        );

        // then
        then(meetingRepository).should(only())
                .deleteAllByUser_IdAndCrew_Id(anyLong(), anyLong());
    }
}