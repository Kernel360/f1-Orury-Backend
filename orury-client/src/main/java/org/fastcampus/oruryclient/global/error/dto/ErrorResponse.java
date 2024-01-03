package org.fastcampus.oruryclient.global.error.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fastcampus.oruryclient.global.error.code.ErrorCode;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;

    @Builder
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
            .status(errorCode.getStatus())
            .message(errorCode.getMessage())
            .build();
    }
}
