package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewDomainFixture {

    private ReviewDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

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
        private @Builder.Default User user = UserDomainFixture.TestUser.createUser(24321L).build().get();
        private @Builder.Default Gym gym = GymDomainFixture.TestGym.createGym(52516L).build().get();
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
        private @Builder.Default UserDto userDto = UserDomainFixture.TestUserDto.createUserDto(101411L).build().get();
        private @Builder.Default GymDto gymDto = GymDomainFixture.TestGymDto.createGymDto(24626L).build().get();
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
}
