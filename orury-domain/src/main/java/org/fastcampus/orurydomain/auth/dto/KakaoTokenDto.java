package org.fastcampus.orurydomain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenDto(
        String accessToken,
        String tokenType,
        String refreshToken,
        String idToken,
        int expiresIn,
        int refreshTokenExpiresIn,
        String scope
) {
    public static KakaoTokenDto of(
            String accessToken,
            String tokenType,
            String refreshToken,
            String idToken,
            int expiresIn,
            int refreshTokenExpiresIn,
            String scope
    ) {
        return new KakaoTokenDto(
                accessToken,
                tokenType,
                refreshToken,
                idToken,
                expiresIn,
                refreshTokenExpiresIn,
                scope
        );
    }
}
