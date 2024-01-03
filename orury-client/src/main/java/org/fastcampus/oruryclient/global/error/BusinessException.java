package org.fastcampus.oruryclient.global.error;

import lombok.Getter;
import org.fastcampus.oruryclient.global.error.code.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}