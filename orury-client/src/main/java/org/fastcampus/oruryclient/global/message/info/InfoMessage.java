package org.fastcampus.oruryapi.global.message.info;

import lombok.Getter;

@Getter
public enum InfoMessage {

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
