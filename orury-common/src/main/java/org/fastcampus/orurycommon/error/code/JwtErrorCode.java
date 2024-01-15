package org.fastcampus.orurycommon.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "토큰이 존재하지 않습니다."),
    TOKEN_NOT_VALID(HttpStatus.NOT_ACCEPTABLE.value(), "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_IS_NULL(4044, "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_NOT_MATCH(4066, "일치하지 않는 토큰입니다.");

    private final int status;
    private final String message;
}
