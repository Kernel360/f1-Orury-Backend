package org.orury.domain.auth.dto;

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
