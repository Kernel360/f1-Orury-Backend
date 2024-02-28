package org.orury.domain.review.domain;

import org.springframework.data.repository.query.Param;

public interface ReviewStore {
    void increaseReactionCount(@Param("reviewId") Long reviewId, @Param("reactionType") int reactionType);

    void decreaseReactionCount(@Param("reviewId") Long reviewId, @Param("reactionType") int reactionType);

    void updateReactionCount(@Param("reviewId") Long reviewId, @Param("oldReactionType") int oldReactionType, @Param("newReactionType") int newReactionType);
}
