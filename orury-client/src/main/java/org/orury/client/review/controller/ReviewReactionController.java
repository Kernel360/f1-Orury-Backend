package org.orury.client.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.review.converter.message.ReviewMessage;
import org.orury.client.review.converter.request.ReviewReactionRequest;
import org.orury.client.review.service.ReviewReactionService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.review.db.model.ReviewReaction;
import org.orury.domain.review.db.model.ReviewReactionPK;
import org.orury.domain.review.dto.ReviewReactionDto;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews/reaction")
@RestController
public class ReviewReactionController {

    private final ReviewReactionService reviewReactionService;

    @Operation(summary = "리뷰 반응 생성/수정/삭제", description = "reviewId를 받아, 리뷰반응을 생성/수정/삭제 한다.")
    @PostMapping
    public ApiResponse<Object> processReviewReaction(@RequestBody ReviewReactionRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(userPrincipal.id(), request.reviewId());
        ReviewReaction reviewReaction = ReviewReaction.of(reactionPK, request.reactionType());
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(reviewReaction);

        reviewReactionService.processReviewReaction(reviewReactionDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_REACTION_PROCESSED.getMessage())
                .build();
    }
}
