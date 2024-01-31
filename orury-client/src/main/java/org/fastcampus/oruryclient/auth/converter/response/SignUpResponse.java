package org.fastcampus.oruryclient.auth.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.auth.dto.SignUpDto;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SignUpResponse(
        Long id,
        String email,
        int signUpType,
        String nickname,
        String accessToken,
        String refreshToken
) {
    public static SignUpResponse of(
            Long id,
            String email,
            int signUpType,
            String nickname,
            String accessToken,
            String refreshToken
    ) {
        return new SignUpResponse(
                id,
                email,
                signUpType,
                nickname,
                accessToken,
                refreshToken
        );
    }

    public static SignUpResponse of(SignUpDto signUpDto) {
        return new SignUpResponse(
                signUpDto.userDto().id(),
                signUpDto.userDto().email(),
                signUpDto.userDto().signUpType(),
                signUpDto.userDto().nickname(),
                signUpDto.jwtToken().accessToken(),
                signUpDto.jwtToken().refreshToken()
        );
    }
}
