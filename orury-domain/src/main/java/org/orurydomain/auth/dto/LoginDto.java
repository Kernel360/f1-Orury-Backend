package org.orurydomain.auth.dto;

import org.orurydomain.global.constants.NumberConstants;
import org.orurydomain.user.dto.UserDto;

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
                        NumberConstants.IS_NOT_DELETED
                ),
                jwtToken,
                flag
        );
    }
}
