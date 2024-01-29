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
}
