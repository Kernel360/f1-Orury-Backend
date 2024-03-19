package org.orury.domain.meeting.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.meeting.domain.MeetingReader;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Reader] 일정 ReaderImpl 테스트")
@ActiveProfiles("test")
class MeetingReaderImplTest {
    MeetingReader meetingReader;
    MeetingRepository meetingRepository;

    @BeforeEach
    void setUp() {
        meetingRepository = mock(MeetingRepository.class);

        meetingReader = new MeetingReaderImpl(meetingRepository);
    }

    @DisplayName("일정id를 받아, 일정을 조회한다.")
    @Test
    void findById() {
        // given & when
        meetingReader.findById(
                mock(Meeting.class).getId()
        );

        // then
        then(meetingRepository).should(only())
                .findById(anyLong());
    }

    @DisplayName("일정id를 받아, 시작되지 않은 일정목록을 조회한다.")
    @Test
    void getNotStartedMeetingsByCrewId() {
        // given & when
        meetingReader.getNotStartedMeetingsByCrewId(
                mock(Crew.class).getId()
        );

        // then
        then(meetingRepository).should(only())
                .findByCrew_IdAndStartTimeAfterOrderByIdDesc(anyLong(), any(LocalDateTime.class));
    }

    @DisplayName("일정id를 받아, 시작된 일정목록을 조회한다.")
    @Test
    void getStartedMeetingsByCrewId() {
        // given & when
        meetingReader.getStartedMeetingsByCrewId(
                mock(Crew.class).getId()
        );

        // then
        then(meetingRepository).should(only())
                .findByCrew_IdAndStartTimeBeforeOrderByIdDesc(anyLong(), any(LocalDateTime.class));
    }
}