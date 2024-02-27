package org.orury.client.gym.interfaces.message;

import lombok.Getter;

@Getter
public enum GymMessage {
    GYM_READ("암장을 정상적으로 조회했습니다."),
    GYM_LIKE_CREATED("암장 좋아요를 완료했습니다."),
    GYM_LIKE_DELETED("암장 좋아요를 취소했습니다.");

    private final String message;

    GymMessage(String message) {
        this.message = message;
    }
}
