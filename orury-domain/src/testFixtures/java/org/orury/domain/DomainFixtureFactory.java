package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.domain.Region;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;
import org.orury.domain.post.domain.entity.PostLikePK;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class DomainFixtureFactory {

    private DomainFixtureFactory() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Getter
    @Builder
    public static class TestUser {
        private @Builder.Default Long id = 1L;
        private @Builder.Default String email = "테스트이메일";
        private @Builder.Default String nickname = "테스트닉네임";
        private @Builder.Default String password = "테스트비밀번호";
        private @Builder.Default int signUpType = 1;
        private @Builder.Default int gender = NumberConstants.MALE;
        private @Builder.Default LocalDate birthday = LocalDate.now().minusYears(25);
        private @Builder.Default String profileImage = "프로필이미지";
        private @Builder.Default UserStatus status = UserStatus.ENABLE;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestUser.TestUserBuilder createUser() {
            return TestUser.builder();
        }

        public static TestUser.TestUserBuilder createUser(Long userId) {
            return TestUser.builder().id(userId);
        }

        public User get() {
            return mapper.convertValue(this, User.class);
        }
    }

    @Getter
    @Builder
    public static class TestUserDto {
        private @Builder.Default Long id = 2L;
        private @Builder.Default String email = "testEamil";
        private @Builder.Default String nickname = "testNickname";
        private @Builder.Default String password = "testPassword";
        private @Builder.Default int signUpType = 2;
        private @Builder.Default int gender = NumberConstants.FEMALE;
        private @Builder.Default LocalDate birthday = LocalDate.now().minusYears(24);
        private @Builder.Default String profileImage = "testProfileImage";
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();
        private @Builder.Default UserStatus status = UserStatus.ENABLE;

        public static TestUserDto.TestUserDtoBuilder createUserDto() {
            return TestUserDto.builder();
        }

        public UserDto get() {
            return mapper.convertValue(this, UserDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrew {
        private @Builder.Default Long id = 3L;
        private @Builder.Default String name = "테스트크루";
        private @Builder.Default int memberCount = 12;
        private @Builder.Default int capacity = 20;
        private @Builder.Default Region region = Region.강남구;
        private @Builder.Default String description = "크루 설명";
        private @Builder.Default String icon = "orury/crew/crew_icon";
        private @Builder.Default CrewStatus status = CrewStatus.ACTIVATED;
        private @Builder.Default User user = TestUser.createUser().build().get();
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

        public Crew get() {
            return mapper.convertValue(this, Crew.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewDto {
        private @Builder.Default Long id = 4L;
        private @Builder.Default String name = "testCrewDto";
        private @Builder.Default int memberCount = 11;
        private @Builder.Default int capacity = 21;
        private @Builder.Default Region region = Region.중구;
        private @Builder.Default String description = "testCrewDescription";
        private @Builder.Default String icon = "testIcon";
        private @Builder.Default CrewStatus status = CrewStatus.ACTIVATED;
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto().build().get();
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

        public CrewDto get() {
            return mapper.convertValue(this, CrewDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewMemberPK {
        private @Builder.Default Long crewId = 5L;
        private @Builder.Default Long userId = 6L;

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
    public static class TestPost {
        private @Builder.Default Long id = 847912L;
        private @Builder.Default String title = "postTitle";
        private @Builder.Default String content = "postContent";
        private @Builder.Default int viewCount = 0;
        private @Builder.Default int commentCount = 0;
        private @Builder.Default int likeCount = 0;
        private @Builder.Default List<String> images = List.of();
        private @Builder.Default int category = 1;
        private @Builder.Default User user = TestUser.createUser(51241L).build().get();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestPost.TestPostBuilder createPost() {
            return TestPost.builder();
        }

        public static TestPost.TestPostBuilder createPost(Long postId) {
            return TestPost.builder().id(postId);
        }

        public Post get() {
            return mapper.convertValue(this, Post.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostDto {
        private @Builder.Default Long id = 123456L;
        private @Builder.Default String title = "postTitle";
        private @Builder.Default String content = "postContent";
        private @Builder.Default int viewCount = 0;
        private @Builder.Default int commentCount = 0;
        private @Builder.Default int likeCount = 0;
        private @Builder.Default List<String> images = List.of();
        private @Builder.Default int category = 1;
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto().build().get();
        private @Builder.Default Boolean isLike = false;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestPostDto.TestPostDtoBuilder createPostDto() {
            return TestPostDto.builder();
        }

        public PostDto get() {
            return mapper.convertValue(this, PostDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostLike {
        private @Builder.Default PostLikePK postLikePK = PostLikePK.of(123456L, 87654321L);

        public static PostLike createPostLike() {
            var postLike = TestPostLike.builder().build();
            return mapper.convertValue(postLike, PostLike.class);
        }

        public static PostLike createPostLike(Long id) {
            var postLike = TestPostLike.builder()
                    .postLikePK(PostLikePK.of(123456L, id))
                    .build();
            return mapper.convertValue(postLike, PostLike.class);
        }
    }
}
