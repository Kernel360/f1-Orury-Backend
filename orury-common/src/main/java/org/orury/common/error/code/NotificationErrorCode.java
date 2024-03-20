package org.orury.common.error.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorCode {
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "알림 권한이 없습니다. "),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "조회된 알람이 없습니다. ");

    private final int status;
    private final String message;
}
