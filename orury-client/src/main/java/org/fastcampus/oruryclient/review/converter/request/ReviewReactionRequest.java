package org.fastcampus.oruryclient.review.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
