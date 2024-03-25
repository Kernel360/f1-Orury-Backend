package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.crew.domain.entity.*;
import org.orury.domain.global.domain.Region;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class CrewDomainFixture {

    private CrewDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestCrew {
        private @Builder.Default Long id = 304254L;
        private @Builder.Default String name = "테스트크루";
        private @Builder.Default int memberCount = 12;
        private @Builder.Default int capacity = 20;
        private @Builder.Default Region region = Region.강남구;
        private @Builder.Default String description = "크루 설명";
        private @Builder.Default String icon = "orury/crew/crew_icon";
        private @Builder.Default CrewStatus status = CrewStatus.ACTIVATED;
        private @Builder.Default User user = UserDomainFixture.TestUser.createUser(1830L).build().get();
        private @Builder.Default int minAge = 15;
        private @Builder.Default int maxAge = 30;
        private @Builder.Default CrewGender gender = CrewGender.ANY;
        private @Builder.Default boolean permissionRequired = false;
        private @Builder.Default String question = null;
        private @Builder.Default boolean answerRequired = false;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestCrew.TestCrewBuilder createCrew() {
            return TestCrew.builder();
        }

        public static TestCrew.TestCrewBuilder createCrew(Long crewId) {
            return TestCrew.builder().id(crewId);
        }

        public Crew get() {
            return mapper.convertValue(this, Crew.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewDto {
        private @Builder.Default Long id = 462623L;
        private @Builder.Default String name = "testCrewDto";
        private @Builder.Default int memberCount = 11;
        private @Builder.Default int capacity = 21;
        private @Builder.Default Region region = Region.중구;
        private @Builder.Default String description = "testCrewDescription";
        private @Builder.Default String icon = "testIcon";
        private @Builder.Default CrewStatus status = CrewStatus.ACTIVATED;
        private @Builder.Default UserDto userDto = UserDomainFixture.TestUserDto.createUserDto(7324L).build().get();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();
        private @Builder.Default int minAge = 14;
        private @Builder.Default int maxAge = 31;
        private @Builder.Default CrewGender gender = CrewGender.MALE;
        private @Builder.Default boolean permissionRequired = true;
        private @Builder.Default String question = "testQuestion";
        private @Builder.Default boolean answerRequired = false;
        private @Builder.Default List<String> tags = List.of("testTag1", "testTag2");

        public static TestCrewDto.TestCrewDtoBuilder createCrewDto() {
            return TestCrewDto.builder();
        }

        public static TestCrewDto.TestCrewDtoBuilder createCrewDto(Long crewId) {
            return TestCrewDto.builder().id(crewId);
        }

        public CrewDto get() {
            return mapper.convertValue(this, CrewDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewMemberPK {
        private @Builder.Default Long crewId = 3525L;
        private @Builder.Default Long userId = 6336L;

        public static TestCrewMemberPK.TestCrewMemberPKBuilder createCrewMemberPK() {
            return TestCrewMemberPK.builder();
        }

        public CrewMemberPK get() {
            return mapper.convertValue(this, CrewMemberPK.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewMember {
        private @Builder.Default CrewMemberPK crewMemberPK = TestCrewMemberPK.createCrewMemberPK().build().get();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestCrewMember.TestCrewMemberBuilder createCrewMember() {
            return TestCrewMember.builder();
        }

        public static TestCrewMember.TestCrewMemberBuilder createCrewMember(Long crewId, Long userId) {
            return TestCrewMember.builder()
                    .crewMemberPK(TestCrewMemberPK.createCrewMemberPK().crewId(crewId).userId(userId).build().get());
        }

        public CrewMember get() {
            return mapper.convertValue(this, CrewMember.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewApplicationPK {
        private @Builder.Default Long crewId = 41819L;
        private @Builder.Default Long userId = 25190L;

        public static TestCrewApplicationPK.TestCrewApplicationPKBuilder createCrewApplicationPK() {
            return TestCrewApplicationPK.builder();
        }

        public CrewApplicationPK get() {
            return mapper.convertValue(this, CrewApplicationPK.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewApplication {
        private @Builder.Default CrewApplicationPK crewApplicationPK = TestCrewApplicationPK.createCrewApplicationPK().build().get();
        private @Builder.Default String answer = "크루 신청 답변";

        public static TestCrewApplication.TestCrewApplicationBuilder createCrewApplication() {
            return TestCrewApplication.builder();
        }

        public static TestCrewApplication.TestCrewApplicationBuilder createCrewApplication(Long crewId, Long userId) {
            return TestCrewApplication.builder()
                    .crewApplicationPK(TestCrewApplicationPK.createCrewApplicationPK().crewId(crewId).userId(userId).build().get());
        }

        public CrewApplication get() {
            return mapper.convertValue(this, CrewApplication.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewApplicationDto {
        private @Builder.Default CrewApplicationPK crewApplicationPK = TestCrewApplicationPK.createCrewApplicationPK().build().get();
        private @Builder.Default String answer = "crewApplicationAnswer";
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestCrewApplicationDto.TestCrewApplicationDtoBuilder createCrewApplicationDto() {
            return TestCrewApplicationDto.builder();
        }

        public static TestCrewApplicationDto.TestCrewApplicationDtoBuilder createCrewApplicationDto(Long crewId, Long userId) {
            return TestCrewApplicationDto.builder()
                    .crewApplicationPK(TestCrewApplicationPK.createCrewApplicationPK().crewId(crewId).userId(userId).build().get());
        }
    }

    @Getter
    @Builder
    public static class TestCrewTag {
        private @Builder.Default Long id = 751814L;
        private @Builder.Default Crew crew = TestCrew.createCrew(1489L).build().get();
        private @Builder.Default String tag = "testTag";

        public static TestCrewTag.TestCrewTagBuilder createCrewTag() {
            return TestCrewTag.builder();
        }

        public CrewTag get() {
            return mapper.convertValue(this, CrewTag.class);
        }
    }
}
