package org.fastcampus.orurydomain.auth.dto.kakao;

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
