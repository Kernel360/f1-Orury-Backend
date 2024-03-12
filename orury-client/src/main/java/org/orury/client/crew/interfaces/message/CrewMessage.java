package org.orury.client.crew.interfaces.message;

import lombok.Getter;

@Getter
public enum CrewMessage {
    CREW_CREATED("크루가 생성됐습니다."),
    CREWS_READ("크루 목록을 조회했습니다."),
    CREW_READ("크루를 조회했습니다.");

    private final String message;

    CrewMessage(String message) {
        this.message = message;
    }
}
