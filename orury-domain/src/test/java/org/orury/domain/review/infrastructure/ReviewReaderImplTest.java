package org.orury.domain.review.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.orury.domain.ReviewDomainFixture.TestReview.createReview;
import static org.orury.domain.ReviewDomainFixture.TestReviewReaction.createReviewReaction;
import static org.orury.domain.ReviewDomainFixture.TestReviewReactionPK.createReviewReactionPK;

@DisplayName("[ReaderImpl] 리뷰 ReaderImpl 테스트")
class ReviewReaderImplTest extends InfrastructureTest {

    @DisplayName("암장 ID를 전달받으면 성공적으로 Review 리스트를 전달해준다.")
    @Test
    void should_RetreiveReviewsSuccessfully() {
        // given
        Long gymId = 1L;
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewRepository.findByGymId(gymId)).willReturn(reviews);

        // when
        reviewReader.findByGymId(gymId);

        // then
        then(reviewRepository).should(times(1))
                .findByGymId(gymId);
    }

    @DisplayName("유저ID와 암장ID를 전달받아 리뷰 존재 여부를 return한다.")
    @Test
    void when_UserIdAndGymId_Then_ReturnIsReviewExists() {
        // given
        Long userId = 1L;
        Long gymId = 1L;

        // when
        reviewReader.existsByUserIdAndGymId(userId, gymId);

        // then
        then(reviewRepository).should(times(1))
                .existsByUserIdAndGymId(userId, gymId);
    }

    @DisplayName("암장ID와 Page 정보를 받아와 최신순으로 리뷰 리스트를 return한다.")
    @Test
    void when_GymIdAndPageable_Then_ReturnReviews() {
        // given
        Long gymId = 1L;
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewRepository.findByGymIdOrderByIdDesc(gymId, pageable)).willReturn(reviews);

        // when
        reviewReader.findByGymIdOrderByIdDesc(gymId, pageable);

        // then
        then(reviewRepository).should(times(1))
                .findByGymIdOrderByIdDesc(gymId, pageable);
    }

    @DisplayName("암장ID와 커서값, Page정보를 받아와 최신순으로 리뷰 리스트를 return한다.")
    @Test
    void when_GymIdAndCursorAndPageable_Then_ReturnReviews() {
        // given
        Long gymId = 1L;
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewRepository.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable))
                .willReturn(reviews);

        // when
        reviewReader.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);

        // then
        then(reviewRepository).should(times(1))
                .findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);
    }

    @DisplayName("유저ID와 Page정보를 받아 내가 작성한 리뷰 리스트를 반환한다.")
    @Test
    void when_UserIdAndPageable_Then_ReturnReviews() {
        // given
        Long userId = 1L;
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewRepository.findByUserIdOrderByIdDesc(userId, pageable))
                .willReturn(reviews);

        // when
        reviewReader.findByUserIdOrderByIdDesc(userId, pageable);

        // then
        then(reviewRepository).should(times(1))
                .findByUserIdOrderByIdDesc(userId, pageable);
    }

    @DisplayName("유저ID와 cursor, Page정보를 받아 내가 작성한 리뷰 리스트를 반환한다.")
    @Test
    void when_UserIdAndCursorAndPageable_Then_ReturnReviews() {
        // given
        Long userId = 1L;
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable))
                .willReturn(reviews);

        // when
        reviewReader.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);

        // then
        then(reviewRepository).should(times(1))
                .findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
    }

    @DisplayName("리뷰ID로 해당 리뷰를 찾아 성공적으로 반환한다.")
    @Test
    void should_ReturnReviewSuccessfully() {
        // given
        Long reviewId = 1L;
        Review review = createReview(reviewId).build().get();

        given(reviewRepository.findById(reviewId))
                .willReturn(Optional.of(review));

        // when
        reviewReader.findById(reviewId);

        // then
        then(reviewRepository).should(times(1))
                .findById(reviewId);
    }

    @DisplayName("리뷰 반응을 수정할 때 기존 리뷰 반응을 반환한다.")
    @Test
    void when_WithReviewReactionPK_Then_ReturnReviewReaction() {
        // given
        Long userId = 1L;
        Long reviewId = 1L;
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        ReviewReaction reviewreaction = createReviewReaction()
                .reviewReactionPK(reactionPK)
                .reactionType(1).build().get();

        given(reviewReactionRepository.findById(reactionPK))
                .willReturn(Optional.of(reviewreaction));

        // when
        reviewReader.findById(reactionPK);

        // then
        then(reviewReactionRepository).should(times(1))
                .findById(reactionPK);
    }

    @DisplayName("유저 ID를 토대로 유저의 리뷰 반응들을 반환한다.")
    @Test
    void when_WithUserId_Then_ReturnReviewReaction() {
        // given
        Long userId = 1L;
        Long reviewId = 1L;
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        List<ReviewReaction> reactions = List.of(
                createReviewReaction().reviewReactionPK(reactionPK).reactionType(1).build().get(),
                createReviewReaction().reviewReactionPK(reactionPK).reactionType(2).build().get(),
                createReviewReaction().reviewReactionPK(reactionPK).reactionType(3).build().get()
        );

        given(reviewReactionRepository.findByReviewReactionPK_UserId(userId))
                .willReturn(reactions);

        // when
        reviewReader.findReviewReactionsByUserId(userId);

        // then
        then(reviewReactionRepository).should(times(1))
                .findByReviewReactionPK_UserId(userId);
    }
}