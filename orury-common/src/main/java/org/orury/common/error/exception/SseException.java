package org.orury.common.error.exception;

import org.orury.common.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class SseException extends RuntimeException {
    private final int status;
    private final String message;

    public SseException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
