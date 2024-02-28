package org.orury.domain.review.domain.dto;

import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;


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
