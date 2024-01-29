package org.fastcampus.orurycommon.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "유저를 찾을 수 없습니다."),
    INVALID_USER(HttpStatus.NOT_FOUND.value(), "유효하지 않은 계정입니다."),
    DUPLICATED_USER(HttpStatus.CONFLICT.value(), "이미 등록된 아이디입니다.");

    private final int status;
    private final String message;
}
