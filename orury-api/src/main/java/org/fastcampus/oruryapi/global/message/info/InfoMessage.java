package org.fastcampus.oruryapi.global.message.info;

public enum InfoMessage {
    POST_CREATED("게시글이 생성되었습니다."),
    POST_DELETED("게시글이 삭제되었습니다."),
    POST_UPDATED("게시글이 수정되었습니다"),
    POST_LIKE_UPDATED("게시글 좋아요 상태가 업데이트 되었습니다."),
    COMMENT_CREATED("댓글이 생성되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    COMMENT_UPDATED("댓글이 수정되었습니다."),
    COMMENT_LIKE_UPDATED("댓글 좋아요 상태가 업데이트 되었습니다.")
    ;

    InfoMessage(String message) {
    }
}