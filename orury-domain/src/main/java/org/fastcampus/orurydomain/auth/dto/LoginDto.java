package org.fastcampus.orurydomain.auth.dto;

import org.fastcampus.orurydomain.user.dto.UserDto;

public record LoginDto(
        UserDto userDto,
        JwtToken jwtToken,
        String flag
) {
    public static LoginDto of(
            UserDto userDto,
            JwtToken jwtToken,
            String flag
    ) {
        return new LoginDto(
                userDto,
                jwtToken,
                flag
        );
    }

    public static LoginDto fromNoUser(
            String email,
            int signUpType,
            JwtToken jwtToken,
            String flag
    ) {
        return new LoginDto(
                UserDto.of(
                        null,
                        email,
                        null,
                        null,
                        signUpType,
                        9,
                        null,
                        null,
                        null,
                        null
                ),
                jwtToken,
                flag
        );
    }
}
