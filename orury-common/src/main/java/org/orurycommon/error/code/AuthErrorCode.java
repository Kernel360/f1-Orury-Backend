package org.orurycommon.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증이 되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다."),
    NO_EMAIL(900, "이메일이 존재하지 않는 회원입니다."),
    NOT_EXISTING_USER_ACCOUNT(910, "해당 이메일로 가입된 계정이 없습니다."),
    NOT_MATCHING_SOCIAL_PROVIDER(920, "이미 가입된 이메일입니다, 다른 소셜로그인으로 진행해 주세요.");

    private final int status;
    private final String message;
}
