package org.fastcampus.oruryclient.review.service;

import org.fastcampus.orurycommon.error.code.ReviewReactionErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.gym.db.model.Gym;
import org.fastcampus.orurydomain.review.db.model.Review;
import org.fastcampus.orurydomain.review.db.model.ReviewReaction;
import org.fastcampus.orurydomain.review.db.model.ReviewReactionPK;
import org.fastcampus.orurydomain.review.db.repository.ReviewReactionRepository;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.review.dto.ReviewReactionDto;
import org.fastcampus.orurydomain.user.db.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewServiceTest")
@ActiveProfiles("test")
class ReviewReactionServiceTest {

    private ReviewReactionService reviewReactionService;
    private ReviewReactionRepository reviewReactionRepository;
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewReactionRepository = mock(ReviewReactionRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        reviewReactionService = new ReviewReactionService(reviewRepository, reviewReactionRepository);
    }

    // method: getReactionType
    @Test
    @DisplayName("userId와 reviewId(ReviewReactionPK)에 해당하는 데이터가 있을 경우, 해당 데이터의 reviewReactionType을 반환한다.")
    void when_ExistingReviewReactionPK_Then_ReturnReactionType() {
        //given
        ReviewReactionPK reviewReactionPK = createReviewReactionPK(1L, 1L);
        ReviewReaction reviewReaction = createReviewReaction(reviewReactionPK, 1);

        given(reviewReactionRepository.findById(any())).willReturn(Optional.of(reviewReaction));

        //when
        int reactionType = reviewReactionService.getReactionType(1L, 1L);

        //then
        then(reviewReactionRepository).should()
                .findById(any());

        assertEquals(1, reactionType);

    }

    @Test
    @DisplayName("userId와 reviewId(ReviewReactionPK)에 해당하는 데이터가 없을 경우, NumberConstants.NOT_REACTION을 반환한다.")
    void when_NotExistingReviewReactionPK_Then_ReturnNumberConstants_NOT_REACTION() {
        //given
        given(reviewReactionRepository.findById(any())).willReturn(Optional.empty());

        //when
        int reactionType = reviewReactionService.getReactionType(1L, 1L);

        //then
        then(reviewReactionRepository).should()
                .findById(any());

        assertEquals(NumberConstants.NOT_REACTION, reactionType);
    }

    @Test
    @DisplayName("유효성 검증: reviewReactionDto.reviewReactionPK().getReviewId() 가 없을 경우 익셉션을 던진다.")
    void when_InvalidReviewId_Then_ThrowBadRequestException() {
        //given
        ReviewReactionPK reviewReactionPK = createReviewReactionPK(1L, 2L);
        ReviewReaction reviewReaction = createReviewReaction(reviewReactionPK, 1);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(reviewReaction);

        given(reviewRepository.findById(anyLong())).willReturn(Optional.empty());

        //when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewReactionService.processReviewReaction(reviewReactionDto));

        assertEquals(ReviewReactionErrorCode.BAD_REQUEST.getStatus(), exception.getStatus());

        then(reviewRepository).should()
                .findById(anyLong());
        then(reviewReactionRepository).should(never())
                .findById(any());
    }

    @Test
    @DisplayName("유효성 검증: reviewReactionDto.reactionType()이 1 미만의 값이 아닌 경우 익셉션을 던진다.")
    void when_UnderReactionType_Then_ThrowBadRequestException() {
        //given
        ReviewReactionPK reviewReactionPK = createReviewReactionPK(1L, 1L);
        ReviewReaction reviewReaction = createReviewReaction(reviewReactionPK, 0);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(reviewReaction);

        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(createReview(1L)));

        //when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewReactionService.processReviewReaction(reviewReactionDto));

        assertEquals(ReviewReactionErrorCode.BAD_REQUEST.getStatus(), exception.getStatus());

        then(reviewRepository).should()
                .findById(anyLong());
        then(reviewReactionRepository).should(never())
                .findById(any());
    }

    @Test
    @DisplayName("유효성 검증: reviewReactionDto.reactionType()이 5 초과의 값이 아닌 경우 익셉션을 던진다.")
    void when_OverReactionType_Then_ThrowBadRequestException() {
        //given
        ReviewReactionPK reviewReactionPK = createReviewReactionPK(1L, 1L);
        ReviewReaction reviewReaction = createReviewReaction(reviewReactionPK, 999);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(reviewReaction);

        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(createReview(1L)));

        //when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewReactionService.processReviewReaction(reviewReactionDto));

        assertEquals(ReviewReactionErrorCode.BAD_REQUEST.getStatus(), exception.getStatus());

        then(reviewRepository).should()
                .findById(anyLong());
        then(reviewReactionRepository).should(never())
                .findById(any());
    }

    @Test
    @DisplayName("유효성이 검증되고, 전에 생성한 reviewReaction이 없을 때, increaseReactionCount와 save를 수행한다.")
    void when_NotExistReviewReaction_Then_IncreaseReviewCountAndSave() {
        //given
        ReviewReactionPK reviewReactionPK = createReviewReactionPK(1L, 1L);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(createReviewReaction(reviewReactionPK, 1));

        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(createReview(1L)));
        given(reviewReactionRepository.findById(any())).willReturn(Optional.empty());

        //when
        reviewReactionService.processReviewReaction(reviewReactionDto);

        //then
        then(reviewRepository).should()
                .findById(anyLong());
        then(reviewReactionRepository).should()
                .findById(any());

        then(reviewRepository).should(times(1))
                .increaseReactionCount(anyLong(), anyInt());
        then(reviewReactionRepository).should(times(1))
                .save(any());

        then(reviewRepository).should(never())
                .updateReactionCount(anyLong(), anyInt(), anyInt());
        then(reviewRepository).should(never())
                .decreaseReactionCount(anyLong(), anyInt());
    }

    @Test
    @DisplayName("유효성이 검증되고, 전에 생성한 reviewReaction이 있을 때, updateReactionCount와 save를 수행한다.")
    void when_ExistReviewReaction_Then_UpdateReviewCountAndSave() {
        //given
        ReviewReactionPK reviewReactionPK = createReviewReactionPK(1L, 1L);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(createReviewReaction(reviewReactionPK, 1));
        ReviewReaction originReviewReaction = createReviewReaction(reviewReactionPK, 3);

        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(createReview(1L)));
        given(reviewReactionRepository.findById(any())).willReturn(Optional.of(originReviewReaction));

        //when
        reviewReactionService.processReviewReaction(reviewReactionDto);

        //then
        then(reviewRepository).should()
                .findById(anyLong());
        then(reviewReactionRepository).should()
                .findById(any());

        then(reviewRepository).should(times(1))
                .updateReactionCount(anyLong(), anyInt(), anyInt());
        then(reviewReactionRepository).should(times(1))
                .save(any());

        then(reviewRepository).should(never())
                .increaseReactionCount(anyLong(), anyInt());
        then(reviewRepository).should(never())
                .decreaseReactionCount(anyLong(), anyInt());
    }

    @Test
    @DisplayName("ReviewReactionPK에 해당하는 ReviewReaction이 있을 때 delete를 수행한다.")
    void when_ExistReactionType_Then_Delete() {
        //given
        ReviewReactionPK reviewReactionPK = createReviewReactionPK(1L, 1L);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(createReviewReaction(reviewReactionPK, 1));
        ReviewReaction originReviewReaction = createReviewReaction(reviewReactionPK, 1);

        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(createReview(1L)));
        given(reviewReactionRepository.findById(any())).willReturn(Optional.of(originReviewReaction));

        //when
        reviewReactionService.processReviewReaction(reviewReactionDto);

        //then
        then(reviewRepository).should()
                .findById(anyLong());
        then(reviewReactionRepository).should()
                .findById(any());

        then(reviewRepository).should(times(1))
                .decreaseReactionCount(anyLong(), anyInt());
        then(reviewReactionRepository).should(times(1))
                .delete(any());

        then(reviewRepository).should(never())
                .increaseReactionCount(anyLong(), anyInt());
        then(reviewRepository).should(never())
                .updateReactionCount(anyLong(), anyInt(), anyInt());
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
