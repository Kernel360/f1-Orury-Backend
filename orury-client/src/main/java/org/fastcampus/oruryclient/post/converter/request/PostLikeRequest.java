package org.fastcampus.oruryclient.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostLikeRequest(
        Long postId
) {
    public static PostLikeRequest of(Long postId) {
        return new PostLikeRequest(postId);
    }
}
