package org.orury.domain.auth.dto;

import org.orury.domain.user.dto.UserDto;

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

