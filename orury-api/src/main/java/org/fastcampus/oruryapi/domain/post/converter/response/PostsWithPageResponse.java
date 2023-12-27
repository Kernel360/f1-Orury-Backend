package org.fastcampus.oruryapi.domain.post.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostsWithPageResponse(
        List<PostsResponse> posts,
        int nextPage
) {
    public static PostsWithPageResponse of(List<PostsResponse> posts, int nextPage) {
        return new PostsWithPageResponse(posts, nextPage);
    }
}
