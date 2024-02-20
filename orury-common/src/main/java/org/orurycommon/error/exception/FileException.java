package org.orurycommon.error.exception;

import lombok.Getter;
import org.orurycommon.error.code.FileExceptionCode;

@Getter
public class FileException extends BusinessException {
    public FileException(final FileExceptionCode errorCode) {
        super(errorCode);
    }
}
