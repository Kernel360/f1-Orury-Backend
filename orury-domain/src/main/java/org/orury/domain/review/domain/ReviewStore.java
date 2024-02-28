package org.orury.domain.review.domain;

import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;

public interface ReviewStore {
    void increaseReactionCount(Long reviewId, int reactionType);

    void decreaseReactionCount(Long reviewId, int reactionType);

    void updateReactionCount(Long reviewId, int oldReactionType, int newReactionType);

    void save(Review review);

    void save(ReviewReaction reviewReaction);

    void delete(Review review);

    void delete(ReviewReaction reviewReaction);
}
