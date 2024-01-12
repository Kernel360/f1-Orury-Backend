package org.fastcampus.oruryclient.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.gym.service.GymService;
import org.fastcampus.oruryclient.review.converter.message.ReviewMessage;
import org.fastcampus.oruryclient.review.converter.request.ReviewCreateRequest;
import org.fastcampus.oruryclient.review.converter.request.ReviewUpdateRequest;
import org.fastcampus.oruryclient.review.converter.response.ReviewResponse;
import org.fastcampus.oruryclient.review.converter.response.ReviewsResponse;
import org.fastcampus.oruryclient.review.converter.response.ReviewsWithCursorResponse;
import org.fastcampus.oruryclient.review.service.ReviewService;
import org.fastcampus.oruryclient.user.service.UserService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final GymService gymService;

    @Operation(summary = "리뷰 생성", description = "requestbody로 리뷰 정보를 받아, 리뷰를 생성한다.")
    @PostMapping("/review")
    public ApiResponse<Object> createReview(@RequestBody ReviewCreateRequest request) {
        UserDto userDto = userService.getUserDtoById(NumberConstants.USER_ID);
        GymDto gymDto = gymService.getGymDtoById(request.gymId());

        reviewService.isExist(userDto, gymDto);

        ReviewDto reviewDto = request.toDto(userDto, gymDto);

        reviewService.createReview(reviewDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_CREATED.getMessage())
                .build();

    }

    @Operation(summary = "리뷰 조회", description = "리뷰 수정을 위해 리뷰 id로 기존에 있는 값을 조회하여 정보를 가져온다.")
    @GetMapping("/review/{reviewId}")
    public ApiResponse<Object> getReview(@PathVariable Long reviewId) {

        ReviewDto reviewDto = reviewService.getReviewDtoById(reviewId);

        ReviewResponse response = ReviewResponse.of(reviewDto, NumberConstants.USER_ID);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 불러온 후, 수정할 리뷰 정보를 받아, 리뷰를 수정한다. ")
    @PatchMapping("/review/{reviewId}")
    public ApiResponse<Object> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequest request) {
        ReviewDto beforeReviewDto = reviewService.getReviewDtoById(reviewId);

        reviewService.isValidate(beforeReviewDto.userDto().id(), NumberConstants.USER_ID);

        ReviewDto updateReviewDto = request.toDto(beforeReviewDto);
        reviewService.updateReview(updateReviewDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 id를 받아, 리뷰를 삭제한다. ")
    @DeleteMapping("/review/{reviewId}")
    public ApiResponse<Object> deleteReview(@PathVariable Long reviewId) {
        ReviewDto reviewDto = reviewService.getReviewDtoById(reviewId);

        reviewService.isValidate(NumberConstants.USER_ID, reviewDto.userDto().id());

        reviewService.deleteReview(reviewDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_DELETED.getMessage())
                .build();
    }

    @Operation(summary = "암장 별 리뷰 조회", description = "암장 id를 받아 해당 암장의 리뷰를 반환한다.")
    @GetMapping("/reviews/{gymId}")
    public ApiResponse<Object> getReviews(@PathVariable Long gymId, @RequestParam Long cursor) {
        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByGymId(gymId, cursor, PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE));
        List<ReviewsResponse> reviewsResponses = reviewDtos.stream()
                .map(reviewDto -> ReviewsResponse.of(reviewDto, NumberConstants.USER_ID))
                .toList();

        ReviewsWithCursorResponse response = ReviewsWithCursorResponse.of(reviewsResponses);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEWS_READ.getMessage())
                .data(response)
                .build();
    }


}
