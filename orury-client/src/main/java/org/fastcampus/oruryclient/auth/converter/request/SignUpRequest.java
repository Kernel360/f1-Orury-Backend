package org.fastcampus.oruryclient.auth.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.user.dto.UserDto;

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
    public static SignUpRequest of(int signUpType, String email, String nickname, int gender, LocalDate birthday, String profileImage) {
        return new SignUpRequest(signUpType, email, nickname, gender, birthday, profileImage);
    }

    public UserDto toDto() {
        return new UserDto(
                null,
                email,
                nickname,
                null,
                signUpType,
                gender,
                birthday,
                profileImage,
                null,
                null
        );
    }
}
