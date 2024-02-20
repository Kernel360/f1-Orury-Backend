package org.fastcampus.oruryclient.auth.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.auth.dto.LoginDto;

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

    public static LoginResponse of(LoginDto loginDto) {
        return LoginResponse.of(
                loginDto.userDto().id(),
                loginDto.userDto().email(),
                loginDto.userDto().signUpType(),
                loginDto.userDto().nickname(),
                loginDto.jwtToken().accessToken(),
                loginDto.jwtToken().refreshToken()
        );
    }

    public static LoginResponse fromNoUser(LoginDto loginDto) {
        return LoginResponse.of(
                null,
                null,
                loginDto.userDto().signUpType(),
                null,
                loginDto.jwtToken().accessToken(),
                null
        );
    }
}
