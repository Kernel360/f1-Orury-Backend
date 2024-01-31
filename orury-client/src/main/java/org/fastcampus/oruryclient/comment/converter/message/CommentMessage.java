package org.fastcampus.oruryclient.comment.converter.message;

import lombok.Getter;

@Getter
public enum CommentMessage {
    COMMENT_CREATED("댓글이 정상적으로 작성되었습니다."),
    COMMENT_READ("댓글이 정상적으로 조회되었습니다."),
    COMMENTS_READ("댓글 목록을 정상적으로 조회했습니다."),
    COMMENT_UPDATED("댓글 수정이 완료되었습니다."),
    COMMENT_DELETED("댓글 삭제가 완료되었습니다."),
    COMMENT_LIKE_CREATED("댓글 좋아요가 완료되었습니다."),
    COMMENT_LIKE_DELETED("댓글 좋아요를 취소했습니다.");

    private final String message;

    CommentMessage(String message) {
        this.message = message;
    }
}
