package org.orury.client.meeting.interfaces.message;

import lombok.Getter;

@Getter
public enum MeetingMessage {
    MEETING_CREATED("일정이 정상적으로 생성되었습니다."),
    MEETINGS_READ("일정 목록을 정상적으로 조회했습니다."),
    MEETING_UPDATED("일정이 정상적으로 수정됐습니다."),
    MEETING_DELETED("일정이 정상적으로 삭제됐습니다."),
    MEETING_MEMBER_ADDED("일정멤버에 정상적으로 추가됐습니다."),
    MEETING_MEMBER_REMOVED("일정멤버에서 정상적으로 제거됐습니다."),
    MEETING_MEMBERS_READ("일정멤버 목록을 정상적으로 조회했습니다.");

    private final String message;

    MeetingMessage(String message) {
        this.message = message;
    }
}
