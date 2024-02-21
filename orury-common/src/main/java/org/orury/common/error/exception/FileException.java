package org.orury.common.error.exception;

import lombok.Getter;
import org.orury.common.error.code.FileExceptionCode;

@Getter
public class FileException extends BusinessException {
    public FileException(final FileExceptionCode errorCode) {
        super(errorCode);
    }
}
