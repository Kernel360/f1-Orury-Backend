package org.fastcampus.oruryclient.auth.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
