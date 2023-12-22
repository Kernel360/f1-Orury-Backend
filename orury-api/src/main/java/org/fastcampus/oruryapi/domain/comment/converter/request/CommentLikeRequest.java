package org.fastcampus.oruryapi.domain.comment.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentLikeRequest(
        Long commentId
) {
    public static CommentLikeRequest of(Long commentId) {
        return new CommentLikeRequest(commentId);
    }
}
