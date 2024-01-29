package org.fastcampus.oruryclient.comment.converter.message;

import lombok.Getter;

@Getter
public enum CommentMessage {
    COMMENT_CREATED("Comment Created"),
    COMMENT_READ("Comment Read"),
    COMMENTS_READ("Comments Read"),
    COMMENT_UPDATED("Comment Updated"),
    COMMENT_DELETED("Comment Deleted"),
    COMMENT_LIKE_CREATED("CommentLike Created"),
    COMMENT_LIKE_DELETED("CommentLike Deleted");

    private final String message;

    CommentMessage(String message) {
        this.message = message;
    }
}
