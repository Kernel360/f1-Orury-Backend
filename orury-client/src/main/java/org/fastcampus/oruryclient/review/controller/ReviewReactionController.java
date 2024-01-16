package org.fastcampus.oruryclient.review.controller;

import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.review.converter.message.ReviewMessage;
import org.fastcampus.oruryclient.review.converter.request.ReviewReactionRequest;
import org.fastcampus.oruryclient.review.service.ReviewReactionService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.review.db.model.ReviewReaction;
import org.fastcampus.orurydomain.review.db.model.ReviewReactionPK;
import org.fastcampus.orurydomain.review.dto.ReviewReactionDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class ReviewReactionController {

    private final ReviewReactionService reviewReactionService;

    @Operation(summary = "반응 생성, 수정", description = "requestbody로 반응 정보를 받아 반응을 생성, 수정한다.")
    @PutMapping("/review/reaction")
    public ApiResponse<Object> createReview(@RequestBody ReviewReactionRequest request) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(NumberConstants.USER_ID, request.reviewId());
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
    public ApiResponse<Object> deleteReview(@PathVariable Long reviewId) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(NumberConstants.USER_ID, reviewId);

        reviewReactionService.deleteReviewReaction(reactionPK);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(ReviewMessage.REVIEW_REACTION_DELETED.getMessage())
                .build();

    }

}
