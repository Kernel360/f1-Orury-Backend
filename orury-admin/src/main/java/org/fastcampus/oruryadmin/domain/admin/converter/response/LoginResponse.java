package org.fastcampus.oruryadmin.domain.admin.converter.response;

import lombok.Builder;
import lombok.Getter;
import org.fastcampus.oruryadmin.global.security.jwt.JwtToken;

@Getter
public class LoginResponse {
    private final String tokenType;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private LoginResponse(String accessToken, String refreshToken) {
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(JwtToken jwtToken) {
        return LoginResponse.builder()
                .accessToken(jwtToken.accessToken())
                .refreshToken(jwtToken.refreshToken())
                .build();
    }
}
