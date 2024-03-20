package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.admin.domain.dto.RoleType;
import org.orury.domain.admin.domain.entity.Admin;
import org.orury.domain.auth.domain.dto.JwtToken;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.auth.domain.dto.SignUpDto;
import org.orury.domain.auth.domain.dto.kakao.KakaoAccount;
import org.orury.domain.auth.domain.dto.kakao.KakaoAccountDto;
import org.orury.domain.auth.domain.dto.kakao.KakaoOAuthTokenDto;
import org.orury.domain.auth.domain.dto.kakao.Profile;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLike;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.domain.Region;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLike;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.meeting.domain.entity.MeetingMemberPK;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.dto.PostLikeDto;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;
import org.orury.domain.post.domain.entity.PostLikePK;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;


public class DomainFixtureFactory {

    private DomainFixtureFactory() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestUser {
        private @Builder.Default Long id = 117143L;
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
        private @Builder.Default Long id = 672622L;
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

        public static TestUserDto.TestUserDtoBuilder createUserDto(Long userId) {
            return TestUserDto.builder().id(userId);
        }

        public UserDto get() {
            return mapper.convertValue(this, UserDto.class);
        }
    }

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
        private @Builder.Default User user = TestUser.createUser(1830L).build().get();
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
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto(7324L).build().get();
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
    public static class TestGym {
        private @Builder.Default Long id = 734136L;
        private @Builder.Default String kakaoId = "암장카카오아이디";
        private @Builder.Default String name = "암장이름";
        private @Builder.Default String roadAddress = "암장도로명주소";
        private @Builder.Default String address = "암장지번주소";
        private @Builder.Default float totalScore = 42.5f;
        private @Builder.Default int reviewCount = 22;
        private @Builder.Default int likeCount = 13;
        private @Builder.Default List<String> images = List.of();
        private @Builder.Default double latitude = 124.456;
        private @Builder.Default double longitude = 124.456;
        private @Builder.Default String brand = "짐브랜드";
        private @Builder.Default String phoneNumber = "010-2234-5678";
        private @Builder.Default String instagramLink = "인스타주소";
        private @Builder.Default String settingDay = "TUESDAY";
        private @Builder.Default String serviceMon = "12:00-23:11";
        private @Builder.Default String serviceTue = "13:00-23:22";
        private @Builder.Default String serviceWed = "14:00-23:33";
        private @Builder.Default String serviceThu = "11:00-23:44";
        private @Builder.Default String serviceFri = "12:00-23:55";
        private @Builder.Default String serviceSat = "13:00-23:66";
        private @Builder.Default String serviceSun = "14:00-23:77";
        private @Builder.Default String homepageLink = "암장홈페이지";
        private @Builder.Default String remark = "암장주석";
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestGym.TestGymBuilder createGym() {
            return TestGym.builder();
        }

        public static TestGym.TestGymBuilder createGym(Long gymId) {
            return TestGym.builder().id(gymId);
        }

        public Gym get() {
            return mapper.convertValue(this, Gym.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymDto {
        private @Builder.Default Long id = 82875L;
        private @Builder.Default String name = "gymKakaoId";
        private @Builder.Default String kakaoId = "gymName";
        private @Builder.Default String roadAddress = "gymRoadAddress";
        private @Builder.Default String address = "gymAddress";
        private @Builder.Default float totalScore = 40.5f;
        private @Builder.Default int reviewCount = 23;
        private @Builder.Default int likeCount = 12;
        private @Builder.Default List<String> images = List.of("gymImage");
        private @Builder.Default double latitude = 123.456;
        private @Builder.Default double longitude = 123.456;
        private @Builder.Default String brand = "gymBrand";
        private @Builder.Default String phoneNumber = "010-1234-5678";
        private @Builder.Default String instagramLink = "gymInstaLink";
        private @Builder.Default String settingDay = "MONDAY";
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();
        private @Builder.Default EnumMap<DayOfWeek, String> businessHours = new EnumMap<>(DayOfWeek.class);
        private @Builder.Default String homepageLink = "gymHomepageLink";
        private @Builder.Default String remark = "gymRemark";

        public static TestGymDto.TestGymDtoBuilder createGymDto() {
            return TestGymDto.builder();
        }

        public static TestGymDto.TestGymDtoBuilder createGymDto(Long gymId) {
            return TestGymDto.builder().id(gymId);
        }

        public GymDto get() {
            return mapper.convertValue(this, GymDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymLikePK {
        private @Builder.Default Long gymId = 9634L;
        private @Builder.Default Long userId = 2128L;

        public static TestGymLikePK.TestGymLikePKBuilder createGymLikePK() {
            return TestGymLikePK.builder();
        }

        public GymLikePK get() {
            return mapper.convertValue(this, GymLikePK.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymLike {
        private @Builder.Default GymLikePK gymLikePK = TestGymLikePK.createGymLikePK().build().get();

        public static TestGymLike.TestGymLikeBuilder createGymLike() {
            return TestGymLike.builder();
        }

        public static TestGymLike.TestGymLikeBuilder createGymLike(Long gymId, Long userId) {
            return TestGymLike.builder().gymLikePK(
                    TestGymLikePK.createGymLikePK()
                            .gymId(gymId)
                            .userId(userId).build().get()
            );
        }

        public GymLike get() {
            return mapper.convertValue(this, GymLike.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymLikeDto {
        private @Builder.Default GymLikePK gymLikePK = TestGymLikePK.createGymLikePK().build().get();

        public static TestGymLikeDto.TestGymLikeDtoBuilder createGymLikeDto() {
            return TestGymLikeDto.builder();
        }

        public static TestGymLikeDto.TestGymLikeDtoBuilder createGymLikeDto(GymLikePK gymLikePK) {
            return TestGymLikeDto.builder().gymLikePK(gymLikePK);
        }

        public static TestGymLikeDto.TestGymLikeDtoBuilder createGymLikeDto(Long gymId, Long userId) {
            return TestGymLikeDto.builder()
                    .gymLikePK(TestGymLikePK.createGymLikePK()
                            .gymId(gymId)
                            .userId(userId).build().get()
                    );
        }

        public GymLikeDto get() {
            return mapper.convertValue(this, GymLikeDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestReview {
        private @Builder.Default Long id = 91234L;
        private @Builder.Default String content = "리뷰 내용";
        private @Builder.Default List<String> images = List.of("image1", "image2", "image3");
        private @Builder.Default float score = 4.5f;
        private @Builder.Default int interestCount = 0;
        private @Builder.Default int likeCount = 1;
        private @Builder.Default int helpCount = 2;
        private @Builder.Default int thumbCount = 3;
        private @Builder.Default int angryCount = 4;
        private @Builder.Default User user = TestUser.createUser(24321L).build().get();
        private @Builder.Default Gym gym = TestGym.createGym(52516L).build().get();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestReview.TestReviewBuilder createReview() {
            return TestReview.builder();
        }

        public static TestReview.TestReviewBuilder createReview(Long reviewId) {
            return TestReview.builder().id(reviewId);
        }

        public Review get() {
            return mapper.convertValue(this, Review.class);
        }
    }

    @Getter
    @Builder
    public static class TestReviewDto {
        private @Builder.Default Long id = 1032452L;
        private @Builder.Default String content = "reviewContent";
        private @Builder.Default List<String> images = List.of("image", "image2", "image33");
        private @Builder.Default float score = 2.5f;
        private @Builder.Default int interestCount = 1;
        private @Builder.Default int likeCount = 2;
        private @Builder.Default int helpCount = 3;
        private @Builder.Default int thumbCount = 4;
        private @Builder.Default int angryCount = 5;
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto(101411L).build().get();
        private @Builder.Default GymDto gymDto = TestGymDto.createGymDto(24626L).build().get();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestReviewDto.TestReviewDtoBuilder createReviewDto() {
            return TestReviewDto.builder();
        }

        public static TestReviewDto.TestReviewDtoBuilder createReviewDto(Long reviewId) {
            return TestReviewDto.builder().id(reviewId);
        }

        public ReviewDto get() {
            return mapper.convertValue(this, ReviewDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestReviewReactionPK {
        private @Builder.Default Long reviewId = 4128L;
        private @Builder.Default Long userId = 2590L;

        public static TestReviewReactionPK.TestReviewReactionPKBuilder createReviewReactionPK() {
            return TestReviewReactionPK.builder();
        }

        public static TestReviewReactionPK.TestReviewReactionPKBuilder createReviewReactionPK(Long reviewId, Long userId) {
            return TestReviewReactionPK.builder()
                    .reviewId(reviewId)
                    .userId(userId);
        }

        public ReviewReactionPK get() {
            return mapper.convertValue(this, ReviewReactionPK.class);
        }
    }

    @Getter
    @Builder
    public static class TestReviewReaction {
        private @Builder.Default ReviewReactionPK reviewReactionPK = TestReviewReactionPK.createReviewReactionPK().build().get();
        private @Builder.Default int reactionType = 2;

        public static TestReviewReaction.TestReviewReactionBuilder createReviewReaction() {
            return TestReviewReaction.builder();
        }

        public static TestReviewReaction.TestReviewReactionBuilder createReviewReaction(Long reviewId, Long userId) {
            return TestReviewReaction.builder().reviewReactionPK(
                    TestReviewReactionPK.createReviewReactionPK()
                            .reviewId(reviewId)
                            .userId(userId).build().get()
            );
        }

        public ReviewReaction get() {
            return mapper.convertValue(this, ReviewReaction.class);
        }
    }

    @Getter
    @Builder
    public static class TestReviewReactionDto {
        private @Builder.Default ReviewReactionPK reviewReactionPK = TestReviewReactionPK.createReviewReactionPK().build().get();
        private @Builder.Default int reactionType = 3;

        public static TestReviewReactionDto.TestReviewReactionDtoBuilder createReviewReactionDto() {
            return TestReviewReactionDto.builder();
        }

        public static TestReviewReactionDto.TestReviewReactionDtoBuilder createReviewReactionDto(ReviewReactionPK reviewReactionPK) {
            return TestReviewReactionDto.builder().reviewReactionPK(reviewReactionPK);
        }

        public static TestReviewReactionDto.TestReviewReactionDtoBuilder createReviewReactionDto(Long reviewId, Long userId) {
            return TestReviewReactionDto.builder()
                    .reviewReactionPK(TestReviewReactionPK.createReviewReactionPK()
                            .reviewId(reviewId)
                            .userId(userId).build().get()
                    );
        }

        public ReviewReactionDto get() {
            return mapper.convertValue(this, ReviewReactionDto.class);
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
        private @Builder.Default List<String> images = List.of("postImage");
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
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto(149009L).build().get();
        private @Builder.Default Boolean isLike = false;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestPostDto.TestPostDtoBuilder createPostDto() {
            return TestPostDto.builder();
        }

        public static TestPostDto.TestPostDtoBuilder createPostDto(Long postId) {
            return TestPostDto.builder().id(postId);
        }

        public PostDto get() {
            return mapper.convertValue(this, PostDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostLikePK {
        private @Builder.Default Long postId = 5269L;
        private @Builder.Default Long userId = 24579L;

        public static TestPostLikePK.TestPostLikePKBuilder createPostLikePK() {
            return TestPostLikePK.builder();
        }

        public PostLikePK get() {
            return mapper.convertValue(this, PostLikePK.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostLike {
        private @Builder.Default PostLikePK postLikePK = TestPostLikePK.createPostLikePK().build().get();

        public static TestPostLike.TestPostLikeBuilder createPostLike() {
            return TestPostLike.builder();
        }

        public static TestPostLike.TestPostLikeBuilder createPostLike(Long postId, Long userId) {
            return TestPostLike.builder().postLikePK(
                    TestPostLikePK.createPostLikePK()
                            .postId(postId)
                            .userId(userId).build().get()
            );
        }

        public PostLike get() {
            return mapper.convertValue(this, PostLike.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostLikeDto {
        private @Builder.Default PostLikePK postLikePK = TestPostLikePK.createPostLikePK().build().get();

        public static TestPostLikeDto.TestPostLikeDtoBuilder createPostLikeDto() {
            return TestPostLikeDto.builder();
        }

        public static TestPostLikeDto.TestPostLikeDtoBuilder createPostLikeDto(PostLikePK postLikePK) {
            return TestPostLikeDto.builder().postLikePK(postLikePK);
        }

        public static TestPostLikeDto.TestPostLikeDtoBuilder createPostLikeDto(Long postId, Long userId) {
            return TestPostLikeDto.builder()
                    .postLikePK(TestPostLikePK.createPostLikePK()
                            .postId(postId)
                            .userId(userId).build().get()
                    );
        }

        public PostLikeDto get() {
            return mapper.convertValue(this, PostLikeDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestComment {
        private @Builder.Default Long id = 87342L;
        private @Builder.Default String content = "댓글내용";
        private @Builder.Default Long parentId = 12L;
        private @Builder.Default int likeCount = 3;
        private @Builder.Default Post post = TestPost.createPost().build().get();
        private @Builder.Default User user = TestUser.createUser().build().get();
        private @Builder.Default int deleted = NumberConstants.IS_NOT_DELETED;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestComment.TestCommentBuilder createComment() {
            return TestComment.builder();
        }

        public static TestComment.TestCommentBuilder createComment(Long commentId) {
            return TestComment.builder().id(commentId);
        }

        public static TestComment.TestCommentBuilder createParentComment() {
            return TestComment.builder().parentId(0L);
        }

        public static TestComment.TestCommentBuilder createChildComment() {
            return TestComment.builder().parentId(11524L);
        }

        public static TestComment.TestCommentBuilder createDeletedComment() {
            return TestComment.builder().deleted(NumberConstants.IS_DELETED);
        }

        public Comment get() {
            return mapper.convertValue(this, Comment.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentDto {
        private @Builder.Default Long id = 87342L;
        private @Builder.Default String content = "댓글내용";
        private @Builder.Default Long parentId = 112L;
        private @Builder.Default int likeCount = 31;
        private @Builder.Default PostDto postDto = TestPostDto.createPostDto().build().get();
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto().build().get();
        private @Builder.Default int deleted = NumberConstants.IS_NOT_DELETED;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestCommentDto.TestCommentDtoBuilder createCommentDto() {
            return TestCommentDto.builder();
        }

        public static TestCommentDto.TestCommentDtoBuilder createCommentDto(Long commentId) {
            return TestCommentDto.builder().id(commentId);
        }

        public static TestCommentDto.TestCommentDtoBuilder createParentCommentDto() {
            return TestCommentDto.builder().parentId(0L);
        }

        public static TestCommentDto.TestCommentDtoBuilder createChildCommentDto(Long parentId) {
            return TestCommentDto.builder().parentId(parentId);
        }

        public static TestCommentDto.TestCommentDtoBuilder createDeletedCommentDto() {
            return TestCommentDto.builder().deleted(NumberConstants.IS_DELETED);
        }

        public CommentDto get() {
            return mapper.convertValue(this, CommentDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentLikePK {
        private @Builder.Default Long commentId = 1234L;
        private @Builder.Default Long userId = 5678L;

        public static TestCommentLikePK.TestCommentLikePKBuilder createCommentLikePK() {
            return TestCommentLikePK.builder();
        }

        public CommentLikePK get() {
            return mapper.convertValue(this, CommentLikePK.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentLike {
        private @Builder.Default CommentLikePK commentLikePK = TestCommentLikePK.createCommentLikePK().build().get();

        public static TestCommentLike.TestCommentLikeBuilder createCommentLike() {
            return TestCommentLike.builder();
        }

        public static TestCommentLike.TestCommentLikeBuilder createCommentLike(Long commentId, Long userId) {
            return TestCommentLike.builder().commentLikePK(
                    TestCommentLikePK.createCommentLikePK()
                            .commentId(commentId)
                            .userId(userId).build().get()
            );
        }

        public CommentLike get() {
            return mapper.convertValue(this, CommentLike.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentLikeDto {
        private @Builder.Default CommentLikePK commentLikePK = TestCommentLikePK.createCommentLikePK().build().get();

        public static TestCommentLikeDto.TestCommentLikeDtoBuilder createCommentLikeDto() {
            return TestCommentLikeDto.builder();
        }

        public static TestCommentLikeDto.TestCommentLikeDtoBuilder createCommentLikeDto(CommentLikePK commentLikePK) {
            return TestCommentLikeDto.builder().commentLikePK(commentLikePK);
        }

        public static TestCommentLikeDto.TestCommentLikeDtoBuilder createCommentLikeDto(Long commentId, Long userId) {
            return TestCommentLikeDto.builder()
                    .commentLikePK(TestCommentLikePK.createCommentLikePK()
                            .commentId(commentId)
                            .userId(userId).build().get()
                    );
        }

        public CommentLikeDto get() {
            return mapper.convertValue(this, CommentLikeDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestMeeting {
        private @Builder.Default Long id = 8035L;
        private @Builder.Default LocalDateTime startTime = LocalDateTime.of(2222, 3, 14, 18, 32);
        private @Builder.Default int memberCount = 1;
        private @Builder.Default int capacity = 5;
        private @Builder.Default User user = TestUser.createUser(60039L).build().get();
        private @Builder.Default Gym gym = TestGym.createGym(621035L).build().get();
        private @Builder.Default Crew crew = TestCrew.createCrew(990852L).build().get();
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
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto(60139L).build().get();
        private @Builder.Default GymDto gymDto = TestGymDto.createGymDto(621015L).build().get();
        private @Builder.Default CrewDto crewDto = TestCrewDto.createCrewDto(190852L).build().get();
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

    @Getter
    @Builder
    public static class TestJwtToken {
        private @Builder.Default String accessToken = "testAccessToken";
        private @Builder.Default String refreshToken = "testRefreshToken";

        public static TestJwtToken.TestJwtTokenBuilder createJwtToken() {
            return TestJwtToken.builder();
        }

        public JwtToken get() {
            return mapper.convertValue(this, JwtToken.class);
        }
    }

    @Getter
    @Builder
    public static class TestSignUpDto {
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto().build().get();
        private @Builder.Default JwtToken jwtToken = TestJwtToken.createJwtToken().build().get();

        public static TestSignUpDto.TestSignUpDtoBuilder createSignUpDto() {
            return TestSignUpDto.builder();
        }

        public SignUpDto get() {
            return mapper.convertValue(this, SignUpDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestLoginDto {
        private @Builder.Default UserDto userDto = TestUserDto.createUserDto().build().get();
        private @Builder.Default JwtToken jwtToken = TestJwtToken.createJwtToken().build().get();
        private @Builder.Default String flag = "testFlag";

        public static TestLoginDto.TestLoginDtoBuilder createLoginDto() {
            return TestLoginDto.builder();
        }

        public LoginDto get() {
            return mapper.convertValue(this, LoginDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestKakaoOAuthTokenDto {
        private @Builder.Default String accessToken = "testAccessToken";
        private @Builder.Default String tokenType = "testTokenType";
        private @Builder.Default String refreshToken = "testRefreshToken";
        private @Builder.Default String idToken = "testIdToken";
        private @Builder.Default int expiresIn = 100;
        private @Builder.Default int refreshTokenExpiresIn = 200;
        private @Builder.Default String scope = "email";

        public static TestKakaoOAuthTokenDto.TestKakaoOAuthTokenDtoBuilder createKakaoOAuthTokenDto() {
            return TestKakaoOAuthTokenDto.builder();
        }

        public KakaoOAuthTokenDto get() {
            return mapper.convertValue(this, KakaoOAuthTokenDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestKakaoAccountDto {
        private @Builder.Default Long id = 1L;
        private @Builder.Default boolean hasSignedUp = true;
        private @Builder.Default LocalDateTime connectedAt = LocalDateTime.of(2024, 2, 8, 22, 00);
        private @Builder.Default LocalDateTime synchedAt = LocalDateTime.of(2024, 2, 8, 22, 00);
        private @Builder.Default KakaoAccount kakaoAccount = new KakaoAccount(new Profile("testNickname"), "test@orury.com");

        public static TestKakaoAccountDto.TestKakaoAccountDtoBuilder createKakaoAccountDto() {
            return TestKakaoAccountDto.builder();
        }

        public KakaoAccountDto get() {
            return mapper.convertValue(this, KakaoAccountDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestAdmin {
        private @Builder.Default Long id = 1L;
        private @Builder.Default String name = "name";
        private @Builder.Default String email = "email";
        private @Builder.Default String password = "pw";
        private @Builder.Default Set<RoleType> roleTypes = Set.of(RoleType.ADMIN);

        public static TestAdmin.TestAdminBuilder createAdmin() {
            return TestAdmin.builder();
        }

        public Admin get() {
            return mapper.convertValue(this, Admin.class);
        }
    }
}
