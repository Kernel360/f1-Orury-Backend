package org.fastcampus.oruryapi.domain.post.converter.response;

import java.util.List;

public record PostsWithCursorResponse(
        List<PostsResponse> posts,
        Long cursor
) {
    public static PostsWithCursorResponse of(List<PostsResponse> posts) {

        Long cursor = (posts.isEmpty())
                ? -1L // -1L
                : posts.get(posts.size() - 1).id();

        return new PostsWithCursorResponse(posts, cursor);
    }
}
