package org.fastcampus.orurycommon.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증이 되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다.");

    private final int status;
    private final String message;
}
