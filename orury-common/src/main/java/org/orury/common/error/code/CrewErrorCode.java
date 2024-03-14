package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CrewErrorCode implements ErrorCode {
    NOT_CREW_MEMBER(HttpStatus.FORBIDDEN.value(), "해당 크루의 크루원이 아닙니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 크루가 존재하지 않습니다."),
    INVALID_STATUS(HttpStatus.NOT_FOUND.value(), "유효하지 않은 크루 Status Code입니다."),
    INVALID_GENDER(HttpStatus.NOT_FOUND.value(), "유효하지 않은 크루 성별 데이터입니다."),
    INVALID_REGION(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 지역입니다."),
    MAXIMUM_PARTICIPATION(HttpStatus.BAD_REQUEST.value(), "최대 3개의 크루에 참여할 수 있습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "크루를 수정/삭제할 권한이 없습니다.");

    private final int status;
    private final String message;
}
