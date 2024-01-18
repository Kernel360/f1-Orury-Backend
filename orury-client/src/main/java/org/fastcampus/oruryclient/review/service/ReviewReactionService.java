package org.fastcampus.oruryclient.review.service;

import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.orurycommon.error.code.ReviewReactionErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.review.db.model.ReviewReaction;
import org.fastcampus.orurydomain.review.db.model.ReviewReactionPK;
import org.fastcampus.orurydomain.review.db.repository.ReviewReactionRepository;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.review.dto.ReviewReactionDto;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ReviewReactionRepository reviewReactionRepository;

    public int getReactionType(Long userId, Long reviewId) {

        ReviewReactionPK reactionPK = ReviewReactionPK.of(userId, reviewId);
        Optional<ReviewReaction> reviewReaction = reviewReactionRepository.findById(reactionPK);

        if (reviewReaction.isEmpty()) return NumberConstants.NOT_REACTION;

        return reviewReaction.get().getReactionType();
    }

    @Transactional
    public void createReviewReaction(ReviewReactionDto reviewReactionDto) {
        userRepository.findById(reviewReactionDto.reviewReactionPK().getUserId())
                .orElseThrow(() -> new BusinessException(ReviewReactionErrorCode.BAD_REQUEST));
        reviewRepository.findById(reviewReactionDto.reviewReactionPK().getReviewId())
                .orElseThrow(() -> new BusinessException(ReviewReactionErrorCode.BAD_REQUEST));

        int reactionType = reviewReactionDto.reactionType();
        if (reactionType < 1 || reactionType > 5) {
            throw new BusinessException(ReviewReactionErrorCode.BAD_REQUEST);
        }
        Optional<ReviewReaction> reviewReaction = reviewReactionRepository.findById(reviewReactionDto.reviewReactionPK());
        if (reviewReaction.isEmpty()) {
            reviewRepository.increaseReviewCount(reviewReactionDto.reviewReactionPK().getReviewId(), reviewReactionDto.reactionType());

        } else {
            int oldReactionType = reviewReaction.get().getReactionType();
            reviewRepository.updateReviewCount(reviewReactionDto.reviewReactionPK().getReviewId(), oldReactionType, reviewReactionDto.reactionType());
        }
        ReviewReaction save_reviewReaction = reviewReactionDto.toEntity();
        reviewReactionRepository.save(save_reviewReaction);
    }

    @Transactional
    public void deleteReviewReaction(ReviewReactionPK reactionPK) {
        Optional<ReviewReaction> reviewReaction = reviewReactionRepository.findById(reactionPK);
        if (reviewReaction.isEmpty()) throw new BusinessException(ReviewReactionErrorCode.NOT_FOUND);

        reviewReactionRepository.delete(reviewReaction.get());
    }
}
