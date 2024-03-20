package org.orury.client.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.FacadeTest;
import org.orury.client.review.interfaces.request.ReviewCreateRequest;
import org.orury.client.review.interfaces.request.ReviewReactionRequest;
import org.orury.client.review.interfaces.request.ReviewUpdateRequest;
import org.orury.client.review.interfaces.response.ReviewsResponse;
import org.orury.client.review.interfaces.response.ReviewsWithCursorResponse;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.orury.client.ClientFixtureFactory.TestReviewCreateRequest.createReviewCreateRequest;
import static org.orury.client.ClientFixtureFactory.TestReviewReactionRequest.createReviewReactionRequest;
import static org.orury.client.ClientFixtureFactory.TestReviewUpdateRequest.createReviewUpdateRequest;
import static org.orury.domain.DomainFixtureFactory.TestGymDto.createGymDto;
import static org.orury.domain.DomainFixtureFactory.TestReviewDto.createReviewDto;
import static org.orury.domain.DomainFixtureFactory.TestReviewReactionPK.createReviewReactionPK;
import static org.orury.domain.DomainFixtureFactory.TestUserDto.createUserDto;

@DisplayName("[Facade] 리뷰 Facade 테스트")
class ReviewFacadeTest extends FacadeTest {

    @DisplayName("리뷰 생성 요청이 들어왔을 때, 리뷰를 성공적으로 저장한다.")
    @Test
    void should_SaveReviewSuccessfully() {
        // given
        Long userId = 1L;
        UserDto userDto = createUserDto().build().get();
        GymDto gymDto = createGymDto().build().get();
        ReviewCreateRequest request = createReviewCreateRequest().build().get();
        List<MultipartFile> images = List.of(mock(MultipartFile.class), mock(MultipartFile.class));

        given(userService.getUserDtoById(userId)).willReturn(userDto);
        given(gymService.getGymDtoById(userId)).willReturn(gymDto);

        // when
        reviewFacade.createReview(userId, request, images);

        // then
        then(userService).should(times(1)).getUserDtoById(any());
        then(gymService).should(times(1)).getGymDtoById(any());
        then(reviewService).should(times(1)).createReview(any(), any());
    }

    @DisplayName("리뷰 수정 요청이 들어왔을 때, 리뷰를 성공적으로 수정한다.")
    @Test
    void should_UpdateReviewSuccessfully() {
        // given
        ReviewUpdateRequest request = createReviewUpdateRequest().build().get();
        Long reviewId = 1L;
        Long userId = 1L;
        List<MultipartFile> images = List.of(mock(MultipartFile.class), mock(MultipartFile.class));
        ReviewDto beforeReviewDto = createReviewDto(reviewId).build().get();
        given(reviewService.getReviewDtoById(reviewId, userId)).willReturn(beforeReviewDto);
        ReviewDto updatedReviewDto = request.toDto(beforeReviewDto);

        // when
        reviewFacade.updateReview(reviewId, userId, request, images);

        // then
        then(reviewService).should(times(1))
                .updateReview(beforeReviewDto, updatedReviewDto, images);
    }

    @DisplayName("리뷰ID와 유저ID를 받아 리뷰를 삭제시킨다.")
    @Test
    void should_DeleteReviewSuccessfully() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        ReviewDto reviewDto = createReviewDto(reviewId).build().get();
        given(reviewService.getReviewDtoById(reviewId, userId)).willReturn(reviewDto);

        // when
        reviewFacade.deleteReview(reviewId, userId);

        // then
        then(reviewService).should(times(1))
                .deleteReview(reviewDto);
    }

    @DisplayName("암장ID, 유저ID, 커서를 받아 암장의 리뷰를 조회한다.")
    @Test
    void when_GetGymIdAndUserIdAndCursor_Then_RetrieveReviews() {
        // given
        Long gymId = 1L;
        Long userId = 1L;
        Long cursor = 1L;
        GymDto gymDto = createGymDto().build().get();
        List<ReviewDto> reviewDtos = List.of(
                createReviewDto(1L).build().get(),
                createReviewDto(2L).build().get(),
                createReviewDto(3L).build().get()
        );
        List<ReviewsResponse> reviewsResponses = reviewDtos.stream()
                .map(reviewDto -> {
                    int myReaction = 1;
                    return ReviewsResponse.of(reviewDto, userId, myReaction);
                })
                .toList();

        given(reviewService.getReactionType(anyLong(), anyLong())).willReturn(1); // 또는 원하는 int 값
        given(gymService.getGymDtoById(gymId)).willReturn(gymDto);
        given(reviewService.getReviewDtosByGymId(gymId, cursor, PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE)))
                .willReturn(reviewDtos);

        ReviewsWithCursorResponse expectResponse = ReviewsWithCursorResponse.of(reviewsResponses, gymDto.name());

        // when
        ReviewsWithCursorResponse response = reviewFacade.getGymReviews(gymId, userId, cursor);

        // then
        assertThat(response).isEqualTo(expectResponse);

        then(gymService).should(times(1))
                .getGymDtoById(gymId);
        then(reviewService).should(times(1))
                .getReviewDtosByGymId(anyLong(), anyLong(), any());
        then(reviewService).should(times(reviewsResponses.size()))
                .getReactionType(anyLong(), anyLong());
    }

    @DisplayName("리뷰 반응 업데이트 요청이 들어오면, 이를 생성/수정/삭제한다.")
    @Test
    void should_ProcessReviewReaction_Successfully() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        ReviewReactionRequest request = createReviewReactionRequest().build().get();
        ReviewReactionPK reactionPK = createReviewReactionPK(reviewId, userId).build().get();
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.of(reactionPK, request.reactionType());

        // when
        reviewFacade.processReviewReaction(reviewId, request, userId);

        // then
        then(reviewService).should(times(1))
                .processReviewReaction(reviewReactionDto);
    }
}