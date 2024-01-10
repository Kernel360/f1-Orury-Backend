package org.fastcampus.oruryclient.gym.error;

import lombok.AllArgsConstructor;
import org.fastcampus.oruryclient.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum GymErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 암장이 존재하지 않습니다.");

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
