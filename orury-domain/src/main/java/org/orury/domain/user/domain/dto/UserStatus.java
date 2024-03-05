package org.orury.domain.user.domain.dto;

import lombok.Getter;

@Getter
public enum UserStatus {
    ENABLE("ROLE_ENABLE", "정상 유저", "E"),
    DISABLE("ROLE_DISABLE", "오래동안 접속 하지 않은 유저", "D"),
    BAN("ROLE_BAN", "정책 위반 으로 제제된 유저", "B"),
    LEAVE("ROLE_LEAVE", "탈퇴한 유저", "L");

    private final String status;
    private final String description;
    private final String code;

    UserStatus(String status, String description, String code) {
        this.status = status;
        this.description = description;
        this.code = code;
    }
}
