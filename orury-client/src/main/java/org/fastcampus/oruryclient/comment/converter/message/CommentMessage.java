package org.fastcampus.oruryclient.comment.converter.message;

import lombok.Getter;

@Getter
public enum CommentMessage {
    COMMENT_CREATED("댓글이 작성되었습니다."),
    COMMENT_READ("댓글이 조회되었습니다."),
    COMMENTS_READ("댓글 목록이 조회되었습니다."),
    COMMENT_UPDATED("댓글이 수정되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    COMMENT_LIKE_CREATED("댓글 좋아요가 생성되었습니다."),
    COMMENT_LIKE_DELETED("댓글 좋아요가 삭제되었습니다.");

    private final String message;

    CommentMessage(String message) {
        this.message = message;
    }
}
