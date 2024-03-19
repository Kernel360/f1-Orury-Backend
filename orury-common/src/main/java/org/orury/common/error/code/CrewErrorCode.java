package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CrewErrorCode implements ErrorCode {
    NOT_CREW_MEMBER(HttpStatus.FORBIDDEN.value(), "해당 크루의 크루원이 아닙니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 크루가 존재하지 않습니다."),
    ALREADY_MEMBER(HttpStatus.BAD_REQUEST.value(), "이미 크루원으로 등록돼 있습니다."),
    NOT_FOUND_APPLICATION(HttpStatus.NOT_FOUND.value(), "존재하지 않는 크루 지원입니다."),
    INVALID_STATUS(HttpStatus.NOT_FOUND.value(), "유효하지 않은 크루 Status Code입니다."),
    INVALID_GENDER(HttpStatus.NOT_FOUND.value(), "유효하지 않은 크루 성별 데이터입니다."),
    INVALID_REGION(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 지역입니다."),
    MAXIMUM_PARTICIPATION(HttpStatus.BAD_REQUEST.value(), "최대 5개의 크루에 참여할 수 있습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "크루를 수정/삭제할 권한이 없습니다."),
    CREATOR_DELETE_FORBIDDEN(HttpStatus.BAD_REQUEST.value(), "크루장은 삭제될 수 없습니다."),
    MEMBER_OVERFLOW(HttpStatus.FORBIDDEN.value(), "크루 인원이 정원을 초과할 수 없습니다."),
    APPLICATION_OVERFLOW(HttpStatus.FORBIDDEN.value(), "크루 지원자 수가 변경하고자 하는 정원을 초과합니다."),
    AGE_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "크루원의 나이는 크루가 설정한 나이 범위 내에 있어야 합니다."),
    GENDER_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "크루원의 성별은 크루가 설정한 성별에 해당돼야 합니다."),
    EMPTY_ANSWER(HttpStatus.BAD_REQUEST.value(), "크루 가입질문에 대한 답변이 작성돼야 합니다.");

    private final int status;
    private final String message;
}
