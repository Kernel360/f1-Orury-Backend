package org.orury.client.crew.interfaces.message;

import lombok.Getter;

@Getter
public enum CrewMessage {
    CREW_CREATED("크루가 생성됐습니다."),
    CREWS_READ("크루 목록을 조회했습니다."),
    CREW_READ("크루를 조회했습니다."),
    CREW_INFO_UPDATED("크루 정보가 변경됐습니다."),
    CREW_IMAGE_UPDATED("크루 이미지가 변경되었습니다."),
    CREW_DELETED("크루가 삭제됐습니다."),
    CREW_APPLIED("크루에 가입신청했습니다."),
    CREW_IMMEDIATELY_JOINED("크루에 바로 가입했습니다."),
    APPLICATION_WITHDRAWN("크루 신청을 철회했습니다."),
    APPLICATION_APPROVED("크루 신청을 승인했습니다."),
    APPLICATION_DISAPPROVED("크루 신청을 거절했습니다."),
    MEMBER_LEAVED("크루를 탈퇴했습니다."),
    MEMBER_EXPELLED("크루원을 강퇴시켰습니다.");

    private final String message;

    CrewMessage(String message) {
        this.message = message;
    }
}
