package org.orury.common.error.exception;

import lombok.Getter;
import org.orury.common.error.code.AuthErrorCode;

@Getter
public class InvalidTokenException extends AuthException {
    public InvalidTokenException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
