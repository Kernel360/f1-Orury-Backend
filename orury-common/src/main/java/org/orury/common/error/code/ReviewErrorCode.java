package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "리뷰가 존재하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "리뷰는 한 번만 작성할 수 있습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "리뷰 수정/삭제 권한이 없습니다.");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
