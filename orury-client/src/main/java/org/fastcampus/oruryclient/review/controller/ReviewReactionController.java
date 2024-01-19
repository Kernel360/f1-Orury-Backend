package org.fastcampus.oruryclient.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.review.converter.message.ReviewMessage;
import org.fastcampus.oruryclient.review.converter.request.ReviewReactionRequest;
import org.fastcampus.oruryclient.review.service.ReviewReactionService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.review.db.model.ReviewReaction;
import org.fastcampus.orurydomain.review.db.model.ReviewReactionPK;
import org.fastcampus.orurydomain.review.dto.ReviewReactionDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class ReviewReactionController {

    private final ReviewReactionService reviewReactionService;

    @Operation(summary = "반응 생성, 수정", description = "requestbody로 반응 정보를 받아 반응을 생성, 수정한다.")
    @PutMapping("/review/reaction")
    public ApiResponse<Object> createReview(@RequestBody ReviewReactionRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(userPrincipal.id(), request.reviewId());
        ReviewReaction reviewReaction = ReviewReaction.of(reactionPK, request.reactionType());
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.from(reviewReaction);

        reviewReactionService.createReviewReaction(reviewReactionDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_REACTION_CREATED.getMessage())
                .build();

    }

    @Operation(summary = "반응 삭제", description = "requestbody로 반응 정보를 받아 반응을 삭제한다.")
    @DeleteMapping("/review/reaction/{reviewId}")
    public ApiResponse<Object> deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(userPrincipal.id(), reviewId);

        reviewReactionService.deleteReviewReaction(reactionPK);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_REACTION_DELETED.getMessage())
                .build();

    }

}
