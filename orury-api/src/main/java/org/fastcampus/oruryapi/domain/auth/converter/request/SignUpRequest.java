package org.fastcampus.oruryapi.domain.auth.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SignUpRequest(
        int signUpType,
        String email,
        String nickname,
        int gender,
        LocalDate birthday,
        String profileImage
        ) {
}
