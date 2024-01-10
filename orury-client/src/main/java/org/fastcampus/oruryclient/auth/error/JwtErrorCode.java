package org.fastcampus.oruryclient.auth.error;

import lombok.AllArgsConstructor;
import org.fastcampus.orurycommon.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum JwtErrorCode implements ErrorCode {
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "토큰이 존재하지 않습니다."),
    TOKEN_NOT_VALID(HttpStatus.NOT_ACCEPTABLE.value(), "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_IS_NULL(4044, "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_NOT_MATCH(4066, "일치하지 않는 토큰입니다."),
    TEMP(999, "임시에러");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
