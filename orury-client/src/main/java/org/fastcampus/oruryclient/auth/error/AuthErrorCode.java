package org.fastcampus.oruryclient.auth.error;

import lombok.AllArgsConstructor;
import org.fastcampus.orurycommon.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "토큰이 존재하지 않습니다."),
    TOKEN_NOT_VALID(HttpStatus.NOT_ACCEPTABLE.value(), "댓글 수정/삭제 권한이 없습니다."),
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
