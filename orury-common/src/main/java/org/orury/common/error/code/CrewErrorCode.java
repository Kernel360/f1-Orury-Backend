package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CrewErrorCode implements ErrorCode {
    NOT_CREW_MEMBER(HttpStatus.FORBIDDEN.value(), "해당 크루의 크루원이 아닙니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 크루가 존재하지 않습니다."),
    INVALID_GENDER(HttpStatus.NOT_FOUND.value(), "유효하지 않은 크루 성별 데이터입니다.");

    private final int status;
    private final String message;
}
