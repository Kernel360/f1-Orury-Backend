package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SseErrorCode implements ErrorCode {


    private final int status;
    private final String message;
}
