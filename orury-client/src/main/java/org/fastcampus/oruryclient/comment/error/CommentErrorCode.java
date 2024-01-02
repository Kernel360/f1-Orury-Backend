package org.fastcampus.oruryclient.comment.error;

import lombok.AllArgsConstructor;
import org.fastcampus.oruryclient.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "댓글이 존재하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "댓글 수정/삭제 권한이 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 댓글 요청입니다.");

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
