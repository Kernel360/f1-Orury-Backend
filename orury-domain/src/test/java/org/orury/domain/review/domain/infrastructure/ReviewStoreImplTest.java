package org.orury.domain.review.domain.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.review.domain.ReviewStore;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.orury.domain.review.infrastructure.ReviewReactionRepository;
import org.orury.domain.review.infrastructure.ReviewRepository;
import org.orury.domain.review.infrastructure.ReviewStoreImpl;
import org.orury.domain.user.domain.entity.User;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("[ReaderImpl] 리뷰 ReaderImpl 테스트")
@ActiveProfiles("test")
class ReviewStoreImplTest {
    private ReviewRepository reviewRepository;
    private ReviewReactionRepository reviewReactionRepository;

    private ReviewStore reviewStore;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        reviewReactionRepository = mock(ReviewReactionRepository.class);
        reviewStore = new ReviewStoreImpl(reviewRepository, reviewReactionRepository);
    }

    @DisplayName("리뷰에 대한 반응의 수를 증가시킨다.")
    @Test
    void should_IncreaseReviewReactionCountSuccessfully() {
        // given
        Long reviewId = 1L;
        int reactionType = 1;

        // when
        reviewStore.increaseReactionCount(reviewId, reactionType);

        // then
        then(reviewRepository).should(times(1))
                .increaseReactionCount(reviewId, reactionType);
    }

    @DisplayName("리뷰에 대한 반응의 수를 감소시킨다.")
    @Test
    void should_DecreaseReviewReactionCountSuccessfully() {
        // given
        Long reviewId = 1L;
        int reactionType = 1;

        // when
        reviewStore.decreaseReactionCount(reviewId, reactionType);

        // then
        then(reviewRepository).should(times(1))
                .decreaseReactionCount(reviewId, reactionType);
    }

    @DisplayName("리뷰에 대한 반응의 수를 업데이트 시킨다.")
    @Test
    void should_UpdateReviewReactionCountSuccessfully() {
        // given
        Long reviewId = 1L;
        int oldReactionType = 1;
        int newReactionType = 2;

        // when
        reviewStore.updateReactionCount(reviewId, oldReactionType, newReactionType);

        // then
        then(reviewRepository).should(times(1))
                .updateReactionCount(reviewId, oldReactionType, newReactionType);
    }

    @DisplayName("리뷰를 성공적으로 저장한다.")
    @Test
    void should_SaveReviewSuccessfully() {
        // given
        Long reviewId = 1L;
        Review review = createReview(reviewId);

        // when
        reviewStore.save(review);

        // then
        then(reviewRepository).should(times(1))
                .save(review);
    }

    @DisplayName("리뷰 반응을 성공적으로 저장한다.")
    @Test
    void should_SaveReviewReactionSuccessfully() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        Review review = createReview(reviewId);
        ReviewReactionPK reactionPK = createReviewReactionPK(userId, reviewId);
        ReviewReaction reviewReaction = createReviewReaction(reactionPK, 1);

        // when
        reviewStore.save(reviewReaction);

        // then
        then(reviewReactionRepository).should(times(1))
                .save(reviewReaction);
    }

    @DisplayName("리뷰를 성공적으로 삭제한다.")
    @Test
    void should_DeleteReviewSuccessfully() {
        // given
        Long reviewId = 1L;
        Review review = createReview(reviewId);

        // when
        reviewStore.delete(review);

        // then
        then(reviewRepository).should(times(1))
                .delete(review);
    }

    @DisplayName("리뷰 반응을 성공적으로 삭제한다.")
    @Test
    void should_DeleteReviewReactionSuccessfully() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        Review review = createReview(reviewId);
        ReviewReactionPK reactionPK = createReviewReactionPK(userId, reviewId);
        ReviewReaction reviewReaction = createReviewReaction(reactionPK, 1);

        // when
        reviewStore.delete(reviewReaction);

        // then
        then(reviewReactionRepository).should(times(1))
                .delete(reviewReaction);
    }

    @DisplayName("유저 id를 받아 리뷰 반응을 모두 제거한다.")
    @Test
    void when_GetUserId_Then_DeleteReviewReactions() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        ReviewReactionPK reactionPK = createReviewReactionPK(userId, reviewId);
        List<ReviewReaction> reactions = List.of(
                createReviewReaction(reactionPK, 1),
                createReviewReaction(reactionPK, 2),
                createReviewReaction(reactionPK, 3)
        );

        given(reviewReactionRepository.findByReviewReactionPK_UserId(userId)).willReturn(reactions);

        // when
        reviewStore.deleteReviewReactionsByUserId(userId);

        // then
        then(reviewRepository).should(times(reactions.size()))
                .decreaseReactionCount(anyLong(), anyInt());
        then(reviewReactionRepository).should(times(reactions.size()))
                .delete(any());
    }


    private static User createUser(Long id) {
        return User.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.now(),
                LocalDateTime.now(),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private static Gym createGym(Long id) {
        return Gym.of(
                id,
                "gymName",
                "gymKakaoId",
                "gymRoadAddress",
                "gymAddress",
                40.5f,
                23,
                12,
                List.of(),
                "123.456",
                "123.456",
                "gymBrand",
                "010-1234-5678",
                "gymInstaLink",
                "MONDAY",
                "11:00-23:11",
                "12:00-23:22",
                "13:00-23:33",
                "14:00-23:44",
                "15:00-23:55",
                "16:00-23:66",
                "17:00-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private static Review createReview(Long id) {
        return Review.of(
                id,
                "reviewContent",
                List.of(),
                4.5f,
                0,
                1,
                2,
                3,
                4,
                createUser(id),
                createGym(id),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static ReviewReactionPK createReviewReactionPK(
            Long userId,
            Long reviewId

    ) {
        return ReviewReactionPK.of(
                userId,
                reviewId
        );
    }

    private static ReviewReaction createReviewReaction(
            ReviewReactionPK reactionPK,
            int reactionType
    ) {
        return ReviewReaction.of(
                reactionPK,
                reactionType
        );
    }
}