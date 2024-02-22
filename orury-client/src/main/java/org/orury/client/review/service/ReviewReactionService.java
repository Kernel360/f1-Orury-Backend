package org.orury.client.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.common.error.code.ReviewReactionErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.review.db.model.ReviewReaction;
import org.orury.domain.review.db.model.ReviewReactionPK;
import org.orury.domain.review.db.repository.ReviewReactionRepository;
import org.orury.domain.review.db.repository.ReviewRepository;
import org.orury.domain.review.dto.ReviewReactionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewReactionService {
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private static final int minReactionType = 1;
    private static final int maxReactionType = 5;

    @Transactional(readOnly = true)
    public int getReactionType(Long userId, Long reviewId) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(userId, reviewId);
        Optional<ReviewReaction> reviewReaction = reviewReactionRepository.findById(reactionPK);

        if (reviewReaction.isEmpty()) return NumberConstants.NOT_REACTION;

        return reviewReaction.get().getReactionType();
    }

    @Transactional
    public void processReviewReaction(ReviewReactionDto reviewReactionDto) {
        reviewRepository.findById(reviewReactionDto.reviewReactionPK().getReviewId())
                .orElseThrow(() -> new BusinessException(ReviewReactionErrorCode.BAD_REQUEST));

        int reactionTypeInput = reviewReactionDto.reactionType();
        if (reactionTypeInput < minReactionType || reactionTypeInput > maxReactionType) {
            throw new BusinessException(ReviewReactionErrorCode.BAD_REQUEST);
        }

        Optional<ReviewReaction> originReaction = reviewReactionRepository.findById(reviewReactionDto.reviewReactionPK());

        if (originReaction.isEmpty()) {
            createReviewReaction(reviewReactionDto);
        } else if (originReaction.get().getReactionType() != reactionTypeInput) {
            updateReviewReaction(reviewReactionDto, originReaction.get().getReactionType());
        } else {
            deleteReviewReaction(reviewReactionDto);
        }
    }

    private void createReviewReaction(ReviewReactionDto reviewReactionDto) {
        reviewRepository.increaseReactionCount(reviewReactionDto.reviewReactionPK().getReviewId(), reviewReactionDto.reactionType());
        reviewReactionRepository.save(reviewReactionDto.toEntity());
    }

    private void updateReviewReaction(ReviewReactionDto reviewReactionDto, int oldReactionType) {
        reviewRepository.updateReactionCount(reviewReactionDto.reviewReactionPK().getReviewId(), oldReactionType, reviewReactionDto.reactionType());
        reviewReactionRepository.save(reviewReactionDto.toEntity());
    }

    private void deleteReviewReaction(ReviewReactionDto reviewReactionDto) {
        reviewRepository.decreaseReactionCount(reviewReactionDto.reviewReactionPK().getReviewId(), reviewReactionDto.reactionType());
        reviewReactionRepository.delete(reviewReactionDto.toEntity());
    }
}
