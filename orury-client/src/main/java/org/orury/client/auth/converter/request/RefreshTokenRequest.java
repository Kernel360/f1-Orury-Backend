package org.orury.client.auth.converter.request;

public record RefreshTokenRequest(
        String refreshToken
) {
    public static RefreshTokenRequest of(
            String refreshToken
    ) {
        return new RefreshTokenRequest(
                refreshToken
        );
    }
}
