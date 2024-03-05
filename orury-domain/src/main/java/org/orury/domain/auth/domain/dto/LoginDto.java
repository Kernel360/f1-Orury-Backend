package org.orury.domain.auth.domain.dto;

import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;

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
            int signUpType,
            JwtToken jwtToken,
            String flag
    ) {
        return new LoginDto(
                UserDto.of(
                        null,
                        null,
                        null,
                        null,
                        signUpType,
                        9,
                        null,
                        null,
                        null,
                        null,
                        UserStatus.E
                ),
                jwtToken,
                flag
        );
    }
}
