package org.orury.domain.global.constants;

import lombok.Getter;

@Getter
public enum Constants {
    // header 종류
    AUTHORIZATION("Authorization"),
    REFRESH_HEADER("Refresh-token"),

    // default values
    DEFAULT_USER_PROFILE("default-user-profile"),
    DEFAULT_NICKNAME("default nickname"),

    // deleted values
    DELETED_USER_NICKNAME("탈퇴 유저"),
    DELETED_COMMENT_CONTENT("삭제된 댓글입니다."),

    // 암장 조회 시, kakaoId 기반으로 kakaoMapLink 만들기 위한 baseUrl
    KAKAO_MAP_BASE_URL("https://map.kakao.com/?itemId=");

    private final String message;

    Constants(String message) {
        this.message = message;
    }

}
