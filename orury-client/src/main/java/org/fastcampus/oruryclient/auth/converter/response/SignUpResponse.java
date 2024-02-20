package org.fastcampus.oruryclient.auth.converter.response;

import org.fastcampus.orurydomain.auth.dto.SignUpDto;

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
