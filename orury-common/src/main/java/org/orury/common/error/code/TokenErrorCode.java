package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCode {
    EXPIRED_ACCESS_TOKEN(990, "만료된 access token 입니다."),
    EXPIRED_REFRESH_TOKEN(999, "만료된 refresh token 입니다."),
    INVALID_ACCESS_TOKEN(980, "유효하지 않은 형식의 access token 입니다."),
    INVALID_REFRESH_TOKEN(989, "유효하지 않은 형식의 refresh token 입니다."),
    EXPIRED_NO_USER_TOKEN(990, "회원가입 시간이 만료됐습니다. 다시 회원가입 해주세요.");
    private final int status;
    private final String message;
}
