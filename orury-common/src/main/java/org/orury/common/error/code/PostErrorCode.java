package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "게시글이 존재하지 않습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT.value(), "조회된 게시글이 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "게시글 수정/삭제 권한이 없습니다.");

    private final int status;
    private final String message;
}
