package org.orury.domain.review.dto;

import org.orury.domain.review.db.model.ReviewReaction;
import org.orury.domain.review.db.model.ReviewReactionPK;


public record ReviewReactionDto(
        ReviewReactionPK reviewReactionPK,
        int reactionType
) {
    private static ReviewReactionDto of(
            ReviewReactionPK reviewReactionPK,
            int reactionType
    ) {
        return new ReviewReactionDto(
                reviewReactionPK,
                reactionType
        );
    }

    public static ReviewReactionDto from(ReviewReaction entity) {
        return ReviewReactionDto.of(
                entity.getReviewReactionPK(),
                entity.getReactionType()
        );
    }

    public ReviewReaction toEntity() {
        return ReviewReaction.of(
                reviewReactionPK,
                reactionType
        );
    }
}
