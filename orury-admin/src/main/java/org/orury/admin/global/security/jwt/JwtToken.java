package org.orury.admin.global.security.jwt;

/**
 * JWT 토큰 Record 클래스
 *
 * @param accessToken
 * @param refreshToken
 */
public record JwtToken(
        String accessToken,
        String refreshToken
) {
    public static JwtToken of(
            String accessToken,
            String refreshToken
    ) {
        return new JwtToken(
                accessToken,
                refreshToken
        );
    }
}
