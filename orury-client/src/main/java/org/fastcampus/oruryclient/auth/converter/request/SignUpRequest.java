package org.fastcampus.oruryclient.auth.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    // UUID 비밀번호를 암호화 시키기 위한 PasswordEncoder
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static SignUpRequest of(int signUpType, String email, String nickname, int gender, LocalDate birthday, String profileImage) {
        return new SignUpRequest(signUpType, email, nickname, gender, birthday, profileImage);
    }

    public UserDto toDto() {
        return new UserDto(
                null,
                email,
                nickname,
                bCryptPasswordEncoder.encode(Integer.toString(signUpType)),
                signUpType,
                gender,
                birthday,
                profileImage,
                null,
                null,
                NumberConstants.IS_NOT_DELETED
        );
    }
}
