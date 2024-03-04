package org.orury.domain.auth.domain.dto.kakao;

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
