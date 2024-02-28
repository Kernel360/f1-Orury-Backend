package org.orury.client.review.interfaces.request;

public record ReviewReactionRequest(
        Long reviewId,
        int reactionType
) {
    public static ReviewReactionRequest of(
            Long reviewId,
            int reactionType
    ) {
        return new ReviewReactionRequest(
                reviewId,
                reactionType
        );
    }
}
