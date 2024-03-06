package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MeetingErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 일정이 존재하지 않습니다."),
    TURNED_OVER_TIMES(HttpStatus.FORBIDDEN.value(), "시작 시각이 종료 시각보다 빨라야 합니다."),
    NOT_SAME_DAY(HttpStatus.FORBIDDEN.value(), "시작과 종료는 같은 날로 지정돼야 합니다."),
    CAPACITY_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "현재 인원이 정원을 초과할 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "일정을 수정/삭제할 권한이 없습니다."),
    ALREADY_JOINED_MEETING(HttpStatus.BAD_REQUEST.value(), "이미 참여하고 있는 일정입니다."),
    NOT_JOINED_MEETING(HttpStatus.BAD_REQUEST.value(), "이미 참여하고 있지 않는 일정입니다."),
    MEETING_CREATOR(HttpStatus.FORBIDDEN.value(), "일정 생성자는 일정멤버에서 삭제될 수 없습니다.");

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
