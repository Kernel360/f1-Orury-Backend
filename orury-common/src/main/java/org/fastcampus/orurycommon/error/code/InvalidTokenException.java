package org.fastcampus.orurycommon.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvalidTokenException implements ErrorCode {
    EXPIRED_TOKEN(990, "만료된 access token 입니다."),
    EXPIRED_REFRESH_TOKEN(999, "만료된 refresh token 입니다."),
    ;
    private final int status;
    private final String message;
}
