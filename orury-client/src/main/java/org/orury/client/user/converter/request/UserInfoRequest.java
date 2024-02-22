package org.orury.client.user.converter.request;

import org.orury.common.util.ImageUrlConverter;
import org.orury.domain.user.dto.UserDto;

public record UserInfoRequest(
        String nickname
) {
    public static UserDto toDto(UserDto userDto, UserInfoRequest userInfoRequest) {
        return UserDto.of(
                userDto.id(),
                userDto.email(),
                userInfoRequest.nickname(),
                userDto.password(),
                userDto.signUpType(),
                userDto.gender(),
                userDto.birthday(),
                ImageUrlConverter.splitUrlToImage(userDto.profileImage()),
                userDto.createdAt(),
                null,
                userDto.isDeleted()
        );
    }
}
