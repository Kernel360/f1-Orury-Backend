package org.fastcampus.oruryapi.domain.comment.error;

import lombok.AllArgsConstructor;
import org.fastcampus.oruryapi.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "댓글이 존재하지 않습니다."),
    UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "댓글을 수정할 수 없습니다."),
    DELETE_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "댓글을 삭제할 수 없습니다.");

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
