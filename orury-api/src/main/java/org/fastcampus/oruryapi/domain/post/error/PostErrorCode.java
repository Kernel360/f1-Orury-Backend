package org.fastcampus.oruryapi.domain.post.error;

import lombok.AllArgsConstructor;
import org.fastcampus.oruryapi.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PostErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "게시글이 존재하지 않습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT.value(), "조회된 게시글이 없습니다.");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
