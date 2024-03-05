package org.orury.domain.user.domain.dto;

import lombok.Getter;

@Getter
public enum UserStatus {
    E("ROLE_USER", "정상 유저"),
    B("ROLE_BAN", "정책 위반 으로 제제된 유저"),
    L("ROLE_LEAVE", "탈퇴한 유저");

    private final String status;
    private final String description;

    UserStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }
}
