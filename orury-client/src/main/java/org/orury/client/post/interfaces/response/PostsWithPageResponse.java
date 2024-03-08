package org.orury.client.post.interfaces.response;

import java.util.List;

public record PostsWithPageResponse(
        List<PostsResponse> posts,
        int nextPage
) {
    public static PostsWithPageResponse of(List<PostsResponse> posts, int nextPage) {
        return new PostsWithPageResponse(posts, nextPage);
    }
}
