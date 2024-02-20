package org.oruryclient.auth.converter.response;

import org.orurydomain.auth.dto.LoginDto;

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
        return new LoginResponse(
                loginDto.userDto().id(),
                loginDto.userDto().email(),
                loginDto.userDto().signUpType(),
                loginDto.userDto().nickname(),
                loginDto.jwtToken().accessToken(),
                loginDto.jwtToken().refreshToken()
        );
    }

    public static LoginResponse fromNoUser(LoginDto loginDto) {
        return new LoginResponse(
                null,
                null,
                loginDto.userDto().signUpType(),
                null,
                loginDto.jwtToken().accessToken(),
                null
        );
    }
}
