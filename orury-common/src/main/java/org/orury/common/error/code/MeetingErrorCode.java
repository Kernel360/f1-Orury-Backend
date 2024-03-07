package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MeetingErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 일정이 존재하지 않습니다."),
    INVALID_START_TIME(HttpStatus.BAD_REQUEST.value(), "시작 시각은 현재 시각 이후로만 설정할 수 있습니다."),
    INVALID_CAPACITY(HttpStatus.BAD_REQUEST.value(), "정원은 2명에서 크루인원수 사이로만 설정할 수 있습니다."),
    CAPACITY_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "현재 인원이 정원을 초과할 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "일정을 수정/삭제할 권한이 없습니다."),
    ALREADY_JOINED_MEETING(HttpStatus.BAD_REQUEST.value(), "이미 참여하고 있는 일정입니다."),
    FULL_MEETING(HttpStatus.FORBIDDEN.value(), "일정 정원을 초과할 수 없습니다."),
    NOT_JOINED_MEETING(HttpStatus.BAD_REQUEST.value(), "이미 참여하고 있지 않는 일정입니다."),
    MEETING_CREATOR(HttpStatus.FORBIDDEN.value(), "일정 생성자는 일정멤버에서 삭제될 수 없습니다.");

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
