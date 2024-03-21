package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.meeting.domain.entity.MeetingMemberPK;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;

public class MeetingDomainFixture {

    private MeetingDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestMeeting {
        private @Builder.Default Long id = 8035L;
        private @Builder.Default LocalDateTime startTime = LocalDateTime.of(2222, 3, 14, 18, 32);
        private @Builder.Default int memberCount = 1;
        private @Builder.Default int capacity = 5;
        private @Builder.Default User user = UserDomainFixture.TestUser.createUser(60039L).build().get();
        private @Builder.Default Gym gym = GymDomainFixture.TestGym.createGym(621035L).build().get();
        private @Builder.Default Crew crew = CrewDomainFixture.TestCrew.createCrew(990852L).build().get();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestMeeting.TestMeetingBuilder createMeeting() {
            return TestMeeting.builder();
        }

        public static TestMeeting.TestMeetingBuilder createMeeting(Long meetingId) {
            return TestMeeting.builder().id(meetingId);
        }

        public Meeting get() {
            return mapper.convertValue(this, Meeting.class);
        }
    }

    @Getter
    @Builder
    public static class TestMeetingDto {
        private @Builder.Default Long id = 18429L;
        private @Builder.Default LocalDateTime startTime = LocalDateTime.of(2024, 3, 20, 15, 30);
        private @Builder.Default int memberCount = 7;
        private @Builder.Default int capacity = 10;
        private @Builder.Default UserDto userDto = UserDomainFixture.TestUserDto.createUserDto(60139L).build().get();
        private @Builder.Default GymDto gymDto = GymDomainFixture.TestGymDto.createGymDto(621015L).build().get();
        private @Builder.Default CrewDto crewDto = CrewDomainFixture.TestCrewDto.createCrewDto(190852L).build().get();
        private @Builder.Default Boolean isParticipated = true;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestMeetingDto.TestMeetingDtoBuilder createMeetingDto() {
            return TestMeetingDto.builder();
        }

        public static TestMeetingDto.TestMeetingDtoBuilder createMeetingDto(Long meetingId) {
            return TestMeetingDto.builder().id(meetingId);
        }

        public MeetingDto get() {
            return mapper.convertValue(this, MeetingDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestMeetingMemberPK {
        private @Builder.Default Long meetingId = 16491L;
        private @Builder.Default Long userId = 53582L;

        public static TestMeetingMemberPK.TestMeetingMemberPKBuilder createMeetingMemberPK() {
            return TestMeetingMemberPK.builder();
        }

        public MeetingMemberPK get() {
            return mapper.convertValue(this, MeetingMemberPK.class);
        }
    }

    @Getter
    @Builder
    public static class TestMeetingMember {
        private @Builder.Default MeetingMemberPK meetingMemberPK = TestMeetingMemberPK.createMeetingMemberPK().build().get();

        public static TestMeetingMember.TestMeetingMemberBuilder createMeetingMember() {
            return TestMeetingMember.builder();
        }

        public static TestMeetingMember.TestMeetingMemberBuilder createMeetingMember(Long meetingId, Long userId) {
            return TestMeetingMember.builder()
                    .meetingMemberPK(TestMeetingMemberPK.createMeetingMemberPK().meetingId(meetingId).userId(userId).build().get());
        }

        public MeetingMember get() {
            return mapper.convertValue(this, MeetingMember.class);
        }
    }
}
