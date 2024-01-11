package org.fastcampus.oruryclient.auth.converter.response;

import org.fastcampus.orurydomain.user.dto.UserDto;

public record LoginResponse(
        Long id,
        String email,
        String nickname,
        String accessToken,
        String refreshToken
) {
    public static LoginResponse of(
            Long id,
            String email,
            String nickname,
            String accessToken,
            String refreshToken
    ) {
        return new LoginResponse(
                id,
                email,
                nickname,
                accessToken,
                refreshToken
        );
    }

    public static LoginResponse of(UserDto userDto) {
        return new LoginResponse(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                "",
                ""
        );
    }
}
