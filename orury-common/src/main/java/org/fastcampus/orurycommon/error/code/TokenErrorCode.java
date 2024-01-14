package org.fastcampus.orurycommon.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCode {
    EXPIRED_ACCESS_TOKEN(990, "만료된 access token 입니다."),
    EXPIRED_REFRESH_TOKEN(999, "만료된 refresh token 입니다."),
    INVALID_ACCESS_TOKEN(980, "유효하지 않은 형식의 access token 입니다.") // 임시 status 값; 990과 동일하게 다룰 것인지?
    ;
    private final int status;
    private final String message;
}
