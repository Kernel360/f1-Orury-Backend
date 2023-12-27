package org.fastcampus.oruryapi.domain.post.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryapi.global.constants.NumberConstants;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostsWithCursorResponse(
        List<PostsResponse> posts,
        Long cursor
) {
    public static PostsWithCursorResponse of(List<PostsResponse> posts) {

        Long cursor = (posts.isEmpty())
                ? NumberConstants.LAST_CURSOR
                : posts.get(posts.size() - 1).id();

        return new PostsWithCursorResponse(posts, cursor);
    }
}
