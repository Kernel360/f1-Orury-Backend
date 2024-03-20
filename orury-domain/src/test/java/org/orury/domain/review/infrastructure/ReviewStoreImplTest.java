package org.orury.domain.review.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.orury.domain.DomainFixtureFactory.TestReview.createReview;
import static org.orury.domain.DomainFixtureFactory.TestReviewReaction.createReviewReaction;
import static org.orury.domain.DomainFixtureFactory.TestReviewReactionPK.createReviewReactionPK;

@ExtendWith(MockitoExtension.class)
@DisplayName("[ReaderImpl] 리뷰 ReaderImpl 테스트")
@ActiveProfiles("test")
class ReviewStoreImplTest extends InfrastructureTest {

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
        Review review = createReview(reviewId).build().get();

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
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        ReviewReaction reviewReaction = createReviewReaction().reviewReactionPK(reactionPK).reactionType(1).build().get();

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
        Review review = createReview(reviewId).build().get();

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
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        ReviewReaction reviewReaction = createReviewReaction().reviewReactionPK(reactionPK).reactionType(1).build().get();

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
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        List<ReviewReaction> reactions = List.of(
                createReviewReaction().reviewReactionPK(reactionPK).reactionType(1).build().get(),
                createReviewReaction().reviewReactionPK(reactionPK).reactionType(2).build().get(),
                createReviewReaction().reviewReactionPK(reactionPK).reactionType(3).build().get()
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
}