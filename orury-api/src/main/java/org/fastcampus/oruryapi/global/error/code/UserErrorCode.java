package org.fastcampus.oruryapi.global.error.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserErrorCode implements ErrorCode{
    NOT_FOUND(HttpStatus.NOT_FOUND.value(),"유저를 찾을 수 없습니다.");

    private final int status;
    private final String message;
    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
