package org.orury.client.review.controller;

import org.orury.client.gym.service.GymService;
import org.orury.client.review.converter.message.ReviewMessage;
import org.orury.client.review.converter.request.ReviewCreateRequest;
import org.orury.client.review.converter.request.ReviewUpdateRequest;
import org.orury.client.review.converter.response.ReviewsResponse;
import org.orury.client.review.converter.response.ReviewsWithCursorResponse;
import org.orury.client.review.service.ReviewReactionService;
import org.orury.client.review.service.ReviewService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.GymService;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.dto.ReviewDto;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewReactionService reviewReactionService;
    private final UserService userService;
    private final GymService gymService;

    @Operation(summary = "리뷰 생성", description = "requestbody로 리뷰 정보를 받아, 리뷰를 생성한다.")
    @PostMapping
    public ApiResponse createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart ReviewCreateRequest request,
            @RequestPart(required = false) List<MultipartFile> image
    ) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        GymDto gymDto = gymService.getGymDtoById(request.gymId());

        reviewService.isExist(userDto, gymDto);

        ReviewDto reviewDto = request.toDto(userDto, gymDto);

        reviewService.createReview(reviewDto, image);

        return ApiResponse.of(ReviewMessage.REVIEW_CREATED.getMessage());
    }

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 불러온 후, 수정할 리뷰 정보를 받아, 리뷰를 수정한다.")
    @PatchMapping("/{reviewId}")
    public ApiResponse updateReview(
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

        return ApiResponse.of(ReviewMessage.REVIEW_UPDATED.getMessage());

    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 id를 받아, 리뷰를 삭제한다.")
    @DeleteMapping("/{reviewId}")
    public ApiResponse deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ReviewDto reviewDto = reviewService.getReviewDtoById(reviewId);

        reviewService.isValidate(userPrincipal.id(), reviewDto.userDto()
                .id());

        reviewService.deleteReview(reviewDto);

        return ApiResponse.of(ReviewMessage.REVIEW_DELETED.getMessage());
    }

    @Operation(summary = "암장 별 리뷰 조회", description = "암장 id를 받아 해당 암장의 리뷰를 반환한다.")
    @GetMapping("/gym/{gymId}")
    public ApiResponse getReviews(@PathVariable Long gymId, @RequestParam Long cursor, @AuthenticationPrincipal UserPrincipal userPrincipal) {
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

        return ApiResponse.of(ReviewMessage.REVIEWS_READ.getMessage(), response);
    }
}
