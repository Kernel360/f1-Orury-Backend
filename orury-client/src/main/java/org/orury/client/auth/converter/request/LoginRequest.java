package org.orury.client.auth.converter.request;

public record LoginRequest(
        String code,
        int signUpType
) {
    public static LoginRequest of(
            String code,
            int signUpType
    ) {
        return new LoginRequest(
                code,
                signUpType
        );
    }
}