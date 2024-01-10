package org.fastcampus.orurycommon.error.exception;

import lombok.Getter;
import org.fastcampus.orurycommon.error.code.FileExceptionCode;

@Getter
public class FileException extends BusinessException {
    public FileException(final FileExceptionCode errorCode) {
        super(errorCode);
    }
}
