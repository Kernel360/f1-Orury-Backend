package org.orury.domain.review.domain.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.review.domain.ReviewStore;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewStoreImpl implements ReviewStore {
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;

    @Override
    public void increaseReactionCount(Long reviewId, int reactionType) {
        reviewRepository.increaseReactionCount(reviewId, reactionType);
    }

    @Override
    public void decreaseReactionCount(Long reviewId, int reactionType) {
        reviewRepository.decreaseReactionCount(reviewId, reactionType);
    }

    @Override
    public void updateReactionCount(Long reviewId, int oldReactionType, int newReactionType) {
        reviewRepository.updateReactionCount(reviewId, oldReactionType, newReactionType);
    }

    @Override
    public void save(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public void save(ReviewReaction reviewReaction) {
        reviewReactionRepository.save(reviewReaction);
    }

    @Override
    public void delete(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public void delete(ReviewReaction reviewReaction) {
        reviewReactionRepository.delete(reviewReaction);
    }
}
