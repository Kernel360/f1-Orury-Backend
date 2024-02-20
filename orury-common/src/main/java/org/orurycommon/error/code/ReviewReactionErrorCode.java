package org.orurycommon.error.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewReactionErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "반응이 존재하지 않습니다. "),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "유효한 요청이 아닙니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "반응 수정/삭제 권한이 없습니다.");

    private final int status;
    private final String message;

}
