package org.orury.client.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ServiceTest;
import org.orury.common.error.code.ReviewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.orury.domain.ReviewDomainFixture.TestReview.createReview;
import static org.orury.domain.ReviewDomainFixture.TestReviewDto.createReviewDto;
import static org.orury.domain.ReviewDomainFixture.TestReviewReaction.createReviewReaction;
import static org.orury.domain.ReviewDomainFixture.TestReviewReactionPK.createReviewReactionPK;

@DisplayName("[ServiceImpl] 리뷰 ServiceImpl 테스트")
class ReviewServiceImplTest extends ServiceTest {

    @DisplayName("리뷰Dto와 이미지가 전달되면, 성공적으로 리뷰를 저장한다.")
    @Test
    void should_ReviewSaveSuccessfully() {
        // given
        ReviewDto reviewDto = createReviewDto().build().get();
        List<MultipartFile> images = List.of(mock(MultipartFile.class), mock(MultipartFile.class));

        // when
        reviewService.createReview(reviewDto, images);

        // then
        then(gymStore).should(times(1))
                .increaseReviewCountAndTotalScore(anyLong(), anyFloat());
    }

    @DisplayName("리뷰 수정 정보를 받아와 리뷰를 수정하고, 리뷰 평점 점수를 업데이트한다.")
    @Test
    void when_GetReviewUpdateInfo_Then_UpdateReviewAndScore() {
        // given
        ReviewDto beforeReviewDto = createReviewDto()
                .content("수정 전").build().get();
        ReviewDto updateReviewDto = createReviewDto()
                .content("수정 후").build().get();

        List<MultipartFile> images = List.of(mock(MultipartFile.class), mock(MultipartFile.class));

        // when
        reviewService.updateReview(beforeReviewDto, updateReviewDto, images);

        // then
        then(gymStore).should(times(1))
                .updateTotalScore(beforeReviewDto.id(), beforeReviewDto.score(), updateReviewDto.score());
        then(imageStore).should(times(1))
                .delete(S3Folder.REVIEW, beforeReviewDto.images());
    }

    @DisplayName("리뷰 ID를 통해 리뷰 DTO를 반환한다.")
    @Test
    void when_GetReviewId_Then_ReturnReviewDto() {
        // given
        Long reviewId = 4141L;
        Review review = createReview(reviewId).build().get();

        given(reviewReader.findById(reviewId)).willReturn(Optional.of(review));

        // when
        reviewService.getReviewDtoById(reviewId, review.getUser().getId());

        // then
        then(reviewReader).should(times(1))
                .findById(anyLong());
    }

    @DisplayName("리뷰 Dto를 받아 리뷰를 성공적으로 삭제한다.")
    @Test
    void should_DeleteReviewSuccessfully() {
        // given
        ReviewDto reviewDto = createReviewDto().build().get();

        // when
        reviewService.deleteReview(reviewDto);

        // then
        then(gymStore).should(times(1))
                .decreaseReviewCountAndTotalScore(anyLong(), anyFloat());
        then(reviewStore).should(times(1))
                .delete(reviewDto.toEntity());
        then(imageStore).should(times(1))
                .delete(S3Folder.REVIEW, reviewDto.images());
    }

    @DisplayName("존재하지 않는 리뷰를 검색 시, NOT_FOUND exception을 발생시킨다.")
    @Test
    void when_NotExistReviewId_Then_ThrowNotFoundException() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;

        given(reviewReader.findById(reviewId))
                .willReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.getReviewDtoById(reviewId, userId));
        assertEquals(ReviewErrorCode.NOT_FOUND.getStatus(), exception.getStatus());

        // then
        then(reviewReader).should(times(1))
                .findById(reviewId);
    }

    @DisplayName("암장ID를 받아, 커서 값이 0인 경우 첫 페이지부터 ReviewDto들을 반환한다.")
    @Test
    void when_GetGymIdAndFirstCursor_Then_ReturnReviewDtos() {
        // given
        Long gymId = 1L;
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewReader.findByGymIdOrderByIdDesc(gymId, pageable)).willReturn(reviews);

        // when
        reviewService.getReviewDtosByGymId(gymId, cursor, pageable);

        // then
        then(reviewReader).should(times(1))
                .findByGymIdOrderByIdDesc(anyLong(), any());
    }

    @DisplayName("암장ID를 받아, 커서 값이 0이 아닌 경우 해당 페이지부터 ReviewDto들을 반환한다.")
    @Test
    void when_GetGymIdAndAfterCursor_Then_ReturnReviewDtos() {
        // given
        Long gymId = 1L;
        Long cursor = 3L;
        Pageable pageable = PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewReader.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable)).willReturn(reviews);

        // when
        reviewService.getReviewDtosByGymId(gymId, cursor, pageable);

        // then
        then(reviewReader).should(times(1))
                .findByGymIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any());
    }

    @DisplayName("유저ID를 받아, 커서 값이 0인 경우 첫 페이지부터 ReviewDto들을 반환한다.")
    @Test
    void when_GetUserIdAndFirstCursor_Then_ReturnReviewDtos() {
        // given
        Long userId = 1L;
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewReader.findByUserIdOrderByIdDesc(userId, pageable)).willReturn(reviews);

        // when
        reviewService.getReviewDtosByUserId(userId, cursor, pageable);

        // then
        then(reviewReader).should(times(1))
                .findByUserIdOrderByIdDesc(anyLong(), any());
    }

    @DisplayName("암장ID를 받아, 커서 값이 0이 아닌 경우 해당 페이지부터 ReviewDto들을 반환한다.")
    @Test
    void when_GetUserIdAndAfterCursor_Then_ReturnReviewDtos() {
        // given
        Long userId = 1L;
        Long cursor = 3L;
        Pageable pageable = PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE);
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewReader.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable)).willReturn(reviews);

        // when
        reviewService.getReviewDtosByUserId(userId, cursor, pageable);

        // then
        then(reviewReader).should(times(1))
                .findByUserIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any());
    }

    @DisplayName("암장 ID를받아 해당 암장의 모든 리뷰를 불러온다.")
    @Test
    void when_GetGymId_Then_ReturnAllReviewDtos() {
        // given
        Long gymId = 1L;
        List<Review> reviews = List.of(
                createReview(1L).build().get(),
                createReview(2L).build().get(),
                createReview(3L).build().get()
        );

        given(reviewReader.findByGymId(gymId)).willReturn(reviews);

        // when
        reviewService.getAllReviewDtosByGymId(gymId);

        // then
        then(reviewReader).should(times(1))
                .findByGymId(anyLong());
    }

    @DisplayName("유저ID와 리뷰ID를 받아 리뷰 반응 정보를 반환한다.")
    @Test
    void when_GetUserIdAndReviewId_Then_ReturnReviewReaction() {
        // given
        Long userId = 1L;
        Long reviewId = 1L;
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        ReviewReaction reviewReaction = createReviewReaction()
                .reviewReactionPK(reactionPK)
                .reactionType(3).build().get();

        given(reviewReader.findById(reactionPK)).willReturn(Optional.of(reviewReaction));

        // when
        int reactionType = reviewService.getReactionType(userId, reviewId);

        // then
        then(reviewReader).should(times(1))
                .findById(reactionPK);
        assertThat(reactionType).isEqualTo(3);
    }

    @DisplayName("리뷰 반응 Dto를 전달받아 리뷰 반응이 없는 경우 생성한다.")
    @Test
    void when_GetReviewDto_Then_CreateReviewReactionSuccessfully() {
        // given
        int reactionTypeInput = 3;
        Long userId = 5121L;
        Long reviewId = 1213L;
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        ReviewReaction reviewReaction = createReviewReaction()
                .reviewReactionPK(reactionPK)
                .reactionType(reactionTypeInput).build().get();
        ReviewDto reviewDto = createReviewDto(reviewId).build().get();
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(reviewReaction);

        given(reviewReader.findById(reviewId)).willReturn(Optional.of(reviewDto.toEntity()));

        // when
        reviewService.processReviewReaction(reviewReactionDto);

        // then
        then(reviewReader).should(times(1))
                .findById(anyLong());
        then(reviewStore).should(times(1))
                .increaseReactionCount(anyLong(), anyInt());
        then(reviewStore).should(times(1))
                .save(reviewReaction);
    }

    @DisplayName("리뷰 반응 Dto를 전달받아 리뷰 반응이 있는 경우 수정한다.")
    @Test
    void when_GetReviewDto_Then_UpdateReviewReactionSuccessfully() {
        // given
        Long userId = 1241L;
        Long reviewId = 126L;
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        Review review = createReview(reviewId).build().get();

        ReviewReaction originReviewReaction = createReviewReaction().reviewReactionPK(reactionPK)
                .reactionType(2).build().get();
        ReviewReaction updateReviewReaction = createReviewReaction().reviewReactionPK(reactionPK)
                .reactionType(3).build().get();

        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(updateReviewReaction);

        given(reviewReader.findById(reactionPK.getReviewId())).willReturn(Optional.of(review));
        given(reviewReader.findById(reactionPK)).willReturn(Optional.of(originReviewReaction));


        // when
        reviewService.processReviewReaction(reviewReactionDto);

        // then
        then(reviewReader).should(times(1))
                .findById(anyLong());
        then(reviewStore).should(times(1))
                .updateReactionCount(reviewId, 2, 3);
        then(reviewStore).should(times(1))
                .save(updateReviewReaction);

        then(reviewStore).should(never())
                .increaseReactionCount(anyLong(), anyInt());
        then(reviewStore).should(never())
                .decreaseReactionCount(anyLong(), anyInt());
    }

    @DisplayName("리뷰 반응 Dto를 전달받아 리뷰 반응이 기존과 동일한 경우 삭제한다.")
    @Test
    void when_GetSameReviewDto_Then_DeleteReviewReactionSuccessfully() {
        // given
        Long userId = 26531L;
        Long reviewId = 1412L;
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        Review review = createReview(reviewId).build().get();

        ReviewReaction originReviewReaction = createReviewReaction().reviewReactionPK(reactionPK)
                .reactionType(3).build().get();
        ReviewReaction updateReviewReaction = createReviewReaction().reviewReactionPK(reactionPK)
                .reactionType(3).build().get();

        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(updateReviewReaction);

        given(reviewReader.findById(anyLong())).willReturn(Optional.of(review));
        given(reviewReader.findById(reactionPK)).willReturn(Optional.of(originReviewReaction));

        // when
        reviewService.processReviewReaction(reviewReactionDto);

        // then
        then(reviewReader).should(times(1))
                .findById(anyLong());
        then(reviewStore).should(times(1))
                .decreaseReactionCount(anyLong(), anyInt());
        then(reviewStore).should(times(1))
                .delete(reviewReactionDto.toEntity());

        then(reviewStore).should(never())
                .updateReactionCount(reviewId, 3, 3);
        then(reviewStore).should(never())
                .increaseReactionCount(anyLong(), anyInt());
        then(reviewStore).should(never())
                .save(updateReviewReaction);
    }
}