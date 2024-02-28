package org.orury.client.review.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.review.application.ReviewFacade;
import org.orury.client.review.interfaces.message.ReviewMessage;
import org.orury.client.review.interfaces.request.ReviewCreateRequest;
import org.orury.client.review.interfaces.request.ReviewReactionRequest;
import org.orury.client.review.interfaces.request.ReviewUpdateRequest;
import org.orury.client.review.interfaces.response.ReviewsWithCursorResponse;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.gym.domain.GymService;
import org.orury.domain.review.domain.ReviewService;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
@RestController
public class ReviewController {
    private final ReviewFacade reviewFacade;
    private final ReviewService reviewService;
    private final UserService userService;
    private final GymService gymService;

    @Operation(summary = "리뷰 생성", description = "requestbody로 리뷰 정보를 받아, 리뷰를 생성한다.")
    @PostMapping
    public ApiResponse createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart ReviewCreateRequest request,
            @RequestPart(required = false) List<MultipartFile> image
    ) {
        reviewFacade.createReview(userPrincipal.id(), request, image);
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
        reviewFacade.updateReview(reviewId, userPrincipal.id(), request, image);
        return ApiResponse.of(ReviewMessage.REVIEW_UPDATED.getMessage());

    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 id를 받아, 리뷰를 삭제한다.")
    @DeleteMapping("/{reviewId}")
    public ApiResponse deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewFacade.deleteReview(reviewId, userPrincipal.id());
        return ApiResponse.of(ReviewMessage.REVIEW_DELETED.getMessage());
    }

    @Operation(summary = "암장 별 리뷰 조회", description = "암장 id를 받아 해당 암장의 리뷰를 반환한다.")
    @GetMapping("/gym/{gymId}")
    public ApiResponse getReviews(@PathVariable Long gymId, @RequestParam Long cursor, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ReviewsWithCursorResponse response = reviewFacade.getGymReviews(gymId, userPrincipal.id(), cursor);
        return ApiResponse.of(ReviewMessage.REVIEWS_READ.getMessage(), response);
    }

    @Operation(summary = "리뷰 반응 생성/수정/삭제", description = "reviewId를 받아, 리뷰반응을 생성/수정/삭제 한다.")
    @PostMapping("/{id}/reaction")
    public ApiResponse processReviewReaction(@PathVariable Long id, @RequestBody ReviewReactionRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reviewFacade.processReviewReaction(id, request, userPrincipal.id());
        return ApiResponse.of(ReviewMessage.REVIEW_REACTION_PROCESSED.getMessage());
    }
}
