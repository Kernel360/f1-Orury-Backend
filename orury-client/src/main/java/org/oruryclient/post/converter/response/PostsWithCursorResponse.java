package org.oruryclient.post.converter.response;

import org.orurydomain.global.constants.NumberConstants;

import java.util.List;

public record PostsWithCursorResponse(
        List<PostsResponse> posts,
        Long cursor
) {
    public static PostsWithCursorResponse of(List<PostsResponse> posts, Long cursor) {

        Long newCursor = (posts.isEmpty())
                ? determineCursorWhenEmpty(cursor)
                : posts.get(posts.size() - 1).id();

        return new PostsWithCursorResponse(posts, newCursor);
    }

    private static Long determineCursorWhenEmpty(Long cursor) {
        return cursor.equals(NumberConstants.FIRST_CURSOR)
                ? NumberConstants.NOTHING_CURSOR
                : NumberConstants.LAST_CURSOR;
    }
}
