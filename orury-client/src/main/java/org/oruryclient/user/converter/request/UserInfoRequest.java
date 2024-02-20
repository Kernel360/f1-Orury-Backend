package org.oruryclient.user.converter.request;

import org.orurycommon.util.ImageUrlConverter;
import org.orurydomain.user.dto.UserDto;

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
