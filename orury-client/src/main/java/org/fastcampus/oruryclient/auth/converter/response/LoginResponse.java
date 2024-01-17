package org.fastcampus.oruryclient.auth.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.auth.jwt.JwtToken;
import org.fastcampus.orurydomain.user.dto.UserDto;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LoginResponse(
        Long id,
        String email,
        int signUpType,
        String nickname,
        String accessToken,
        String refreshToken
) {
    public static LoginResponse of(
            Long id,
            String email,
            int signUpType,
            String nickname,
            String accessToken,
            String refreshToken
    ) {
        return new LoginResponse(
                id,
                email,
                signUpType,
                nickname,
                accessToken,
                refreshToken
        );
    }

    public static LoginResponse of(UserDto userDto, JwtToken jwtToken) {
        return new LoginResponse(
                userDto.id(),
                userDto.email(),
                userDto.signUpType(),
                userDto.nickname(),
                jwtToken.accessToken(),
                jwtToken.refreshToken()
        );
    }
}
