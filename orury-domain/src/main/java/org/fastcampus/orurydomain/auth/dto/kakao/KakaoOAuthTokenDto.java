package org.fastcampus.orurydomain.auth.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoOAuthTokenDto(
        String accessToken,
        String tokenType,
        String refreshToken,
        String idToken,
        int expiresIn,
        int refreshTokenExpiresIn,
        String scope
) {

}
