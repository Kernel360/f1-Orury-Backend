package org.fastcampus.oruryapi.global.constants;

import lombok.Getter;

@Getter
public enum Constants {
    ADMIN("admin"),
    SYSTEM("system"),
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),

    // 초기 커서 입력값
    FIRST_CURSOR("0"),

    // 조회된 목록이 없음을 나타내는, 마지막 커서 반환값
    LAST_CURSOR("-1"),

    // 페이지네이션 단위
    PAGINATION_SIZE("10"),

    // userInfo 등 하드 코딩된 것 상수로
    USERID("1L"),

    // header 종류
    AUTHORIZATION("Authorization"),
    REFRESH_HEADER("Refresh-token")


    ;

    private final String message;

    Constants(String message) {
        this.message = message;
    }

}
