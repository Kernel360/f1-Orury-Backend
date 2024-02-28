package org.orury.client.review.interfaces.request;

public record ReviewReactionRequest(
        int reactionType
) {
    public static ReviewReactionRequest of(
            int reactionType
    ) {
        return new ReviewReactionRequest(
                reactionType
        );
    }
}
