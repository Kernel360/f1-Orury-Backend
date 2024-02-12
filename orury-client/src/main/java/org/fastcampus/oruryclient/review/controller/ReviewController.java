package org.fastcampus.oruryclient.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.gym.service.GymService;
import org.fastcampus.oruryclient.review.converter.message.ReviewMessage;
import org.fastcampus.oruryclient.review.converter.request.ReviewCreateRequest;
import org.fastcampus.oruryclient.review.converter.request.ReviewUpdateRequest;
import org.fastcampus.oruryclient.review.converter.response.ReviewsResponse;
import org.fastcampus.oruryclient.review.converter.response.ReviewsWithCursorResponse;
import org.fastcampus.oruryclient.review.service.ReviewReactionService;
import org.fastcampus.oruryclient.review.service.ReviewService;
import org.fastcampus.oruryclient.user.service.UserService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor

//
@RequestMapping("/api/v1/reviews")
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewReactionService reviewReactionService;
    private final UserService userService;
    private final GymService gymService;

    //
    @Operation(summary = "리뷰 생성", description = "requestbody로 리뷰 정보를 받아, 리뷰를 생성한다.")
    @PostMapping
    public ApiResponse<Object> createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart ReviewCreateRequest request,
            @RequestPart(required = false) List<MultipartFile> image
    ) {
        //
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        GymDto gymDto = gymService.getGymDtoById(request.gymId());

        reviewService.isExist(userDto, gymDto);

        ReviewDto reviewDto = request.toDto(userDto, gymDto);

        reviewService.createReview(reviewDto, image);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 불러온 후, 수정할 리뷰 정보를 받아, 리뷰를 수정한다.")
    @PatchMapping("/{reviewId}")
    public ApiResponse<Object> updateReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart ReviewUpdateRequest request,
            @RequestPart(required = false) List<MultipartFile> image
    ) {
        ReviewDto beforeReviewDto = reviewService.getReviewDtoById(reviewId);

        reviewService.isValidate(beforeReviewDto.userDto()
                .id(), userPrincipal.id());

        ReviewDto updateReviewDto = request.toDto(beforeReviewDto);
        reviewService.updateReview(beforeReviewDto, updateReviewDto, image);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 id를 받아, 리뷰를 삭제한다.")
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Object> deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ReviewDto reviewDto = reviewService.getReviewDtoById(reviewId);

        reviewService.isValidate(userPrincipal.id(), reviewDto.userDto()
                .id());

        reviewService.deleteReview(reviewDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_DELETED.getMessage())
                .build();
    }

    @Operation(summary = "암장 별 리뷰 조회", description = "암장 id를 받아 해당 암장의 리뷰를 반환한다.")
    @GetMapping("/gym/{gymId}")
    public ApiResponse<Object> getReviews(@PathVariable Long gymId, @RequestParam Long cursor, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String gymName = gymService.getGymDtoById(gymId)
                .name();
        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByGymId(gymId, cursor, PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE));
        List<ReviewsResponse> reviewsResponses = reviewDtos.stream()
                .map(reviewDto -> {
                    int myReaction = reviewReactionService.getReactionType(userPrincipal.id(), reviewDto.id());
                    UserDto userDto = userService.getUserDtoById(reviewDto.userDto().id());
                    return ReviewsResponse.of(reviewDto, userDto, myReaction);
                })
                .toList();

        ReviewsWithCursorResponse response = ReviewsWithCursorResponse.of(reviewsResponses, gymName);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEWS_READ.getMessage())
                .data(response)
                .build();
    }
}
