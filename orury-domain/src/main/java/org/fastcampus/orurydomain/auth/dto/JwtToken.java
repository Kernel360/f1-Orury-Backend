package org.fastcampus.orurydomain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
