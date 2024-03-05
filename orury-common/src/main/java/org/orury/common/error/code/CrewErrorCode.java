package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CrewErrorCode implements ErrorCode {
    NOT_CREW_MEMBER(HttpStatus.FORBIDDEN.value(), "해당 크루의 크루원이 아닙니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 크루가 존재하지 않습니다.");

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
