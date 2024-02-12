package org.fastcampus.oruryclient.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurycommon.error.code.ReviewReactionErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.review.db.model.ReviewReaction;
import org.fastcampus.orurydomain.review.db.model.ReviewReactionPK;
import org.fastcampus.orurydomain.review.db.repository.ReviewReactionRepository;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.review.dto.ReviewReactionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewReactionService {
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;

    @Transactional(readOnly = true)
    public int getReactionType(Long userId, Long reviewId) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(userId, reviewId);
        Optional<ReviewReaction> reviewReaction = reviewReactionRepository.findById(reactionPK);

        if (reviewReaction.isEmpty()) return NumberConstants.NOT_REACTION;

        return reviewReaction.get().getReactionType();
    }

    @Transactional
    public void processReviewReaction(ReviewReactionDto reviewReactionDto) {

        //
        reviewRepository.findById(reviewReactionDto.reviewReactionPK().getReviewId())
                .orElseThrow(() -> new BusinessException(ReviewReactionErrorCode.BAD_REQUEST));

        //
        int reactionTypeInput = reviewReactionDto.reactionType();

        // Request에 대한 정책은 CustomValidator에
        if (reactionTypeInput < 1 || reactionTypeInput > 5) {
            throw new BusinessException(ReviewReactionErrorCode.BAD_REQUEST);
        }

        Optional<ReviewReaction> originReaction = reviewReactionRepository.findById(reviewReactionDto.reviewReactionPK());

        //
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
