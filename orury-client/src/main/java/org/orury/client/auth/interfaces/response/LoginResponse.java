package org.orury.client.auth.interfaces.response;

import org.orury.domain.auth.domain.dto.LoginDto;

import java.util.Objects;

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

    public boolean isNoUser() {
        return (Objects.isNull(id)
                && Objects.isNull(email)
                && Objects.isNull(nickname)
                && Objects.isNull(refreshToken)
        );
    }
}
