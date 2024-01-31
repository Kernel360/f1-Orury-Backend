package org.fastcampus.orurydomain.auth.dto;

import org.fastcampus.orurydomain.user.dto.UserDto;

public record SignUpDto(
        UserDto userDto,
        JwtToken jwtToken
) {
    public static SignUpDto of(
            UserDto userDto,
            JwtToken jwtToken
    ) {
        return new SignUpDto(
                userDto,
                jwtToken
        );
    }
}

