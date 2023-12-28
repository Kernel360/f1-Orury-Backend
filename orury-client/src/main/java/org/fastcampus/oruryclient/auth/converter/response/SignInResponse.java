package org.fastcampus.oruryclient.auth.converter.response;

import org.fastcampus.orurydomain.user.dto.UserDto;

public record SignInResponse(
        Long id,
        String email,
        String nickname,
        String accessToken,
        String refreshToken
) {
    public static SignInResponse of(
            Long id,
            String email,
            String nickname,
            String accessToken,
            String refreshToken
    ) {
        return new SignInResponse(
                id,
                email,
                nickname,
                accessToken,
                refreshToken
        );
    }

    public static SignInResponse of(UserDto userDto) {
        return new SignInResponse(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                "",
                ""
        );
    }
}
