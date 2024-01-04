package org.fastcampus.oruryclient.global.error;

import lombok.Getter;
import org.fastcampus.oruryclient.global.error.code.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final int status;
    private final String message;

    public BusinessException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}