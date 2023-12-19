package org.fastcampus.oruryapi.global.message.info;

import lombok.Getter;

@Getter
public enum InfoMessage {

    // 게시글
    POST_CREATED("게시글이 생성되었습니다."),
    POST_READ("게시글을 조회했습니다."),
    POSTS_READ("게시글 목록을 조회했습니다."),
    POST_UPDATED("게시글이 수정되었습니다"),
    POST_DELETED("게시글이 삭제되었습니다."),
    POST_LIKE_CREATED("게시글 좋아요가 생성되었습니다."),
    POST_LIKE_DELETED("게시글 좋아요가 삭제되었습니다."),

    // 댓글
    COMMENT_CREATED("댓글이 생성되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    COMMENT_UPDATED("댓글이 수정되었습니다."),
    COMMENT_LIKE_UPDATED("댓글 좋아요 상태가 업데이트 되었습니다."),

    // 유저
    USER_LOGIN_SUCCESS("정상적으로 로그인 되었습니다."),
    USER_SIGNUP_SUCCESS("회원가입이 정상적으로 완료되었습니다.")
    ;

    private final String message;

    InfoMessage(String message) {
        this.message = message;
    }

}
