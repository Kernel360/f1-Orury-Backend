package org.fastcampus.oruryclient.auth.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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