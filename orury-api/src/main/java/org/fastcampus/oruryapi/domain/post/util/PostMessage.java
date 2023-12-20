package org.fastcampus.oruryapi.domain.post.util;

import lombok.Getter;

@Getter
public enum PostMessage {
    POST_CREATED("Post Created"),
    POST_READ("Post Read"),
    POSTS_READ("Posts Read"),
    POST_UPDATED("Post Updated"),
    POST_DELETED("Post Deleted"),
    POST_LIKE_CREATED("PostLike Created"),
    POST_LIKE_DELETED("PostLike Deleted");

    private final String message;

    PostMessage(String message) {
        this.message = message;
    }
}
