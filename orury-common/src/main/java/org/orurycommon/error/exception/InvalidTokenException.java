package org.orurycommon.error.exception;

import lombok.Getter;
import org.orurycommon.error.code.AuthErrorCode;

@Getter
public class InvalidTokenException extends AuthException {
    public InvalidTokenException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
