package org.fastcampus.oruryclient.auth.util;

import lombok.Getter;

@Getter
public enum AuthMessage {

    LOGIN_SUCCESS("정상적으로 로그인 되었습니다."),
    SIGNUP_SUCCESS("정상적으로 회원가입 되었습니다."),
    REISSUE_ACCESS_TOKEN_SUCCESS("Access 토큰이 재발급되었습니다.");

    private final String message;

    AuthMessage(String message) {
        this.message = message;
    }

}
