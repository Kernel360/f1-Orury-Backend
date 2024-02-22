package org.orury.client.auth.converter.message;

import lombok.Getter;

@Getter
public enum AuthMessage {

    LOGIN_SUCCESS("정상적으로 로그인 되었습니다."),
    SIGNUP_SUCCESS("정상적으로 회원가입 되었습니다."),
    REISSUE_ACCESS_TOKEN_SUCCESS("Access 토큰이 재발급되었습니다."),
    NOT_EXISTING_USER_ACCOUNT("해당 이메일로 가입된 계정이 없습니다."),
    NOT_MATCHING_SOCIAL_PROVIDER("이미 가입된 이메일입니다. 다른 소셜로그인으로 진행해 주세요.");

    private final String message;

    AuthMessage(String message) {
        this.message = message;
    }

}
