package org.fastcampus.oruryapi.global.message.error;

public enum ErrorMessage {

    // 게시글
    POST_NO_POST("게시글이 존재하지 않습니다."),

    // 유저
    USER_INVALID_ACCOUNT("유효하지 않은 계정입니다.")
    ;

    ErrorMessage(String message) {
    }
}
