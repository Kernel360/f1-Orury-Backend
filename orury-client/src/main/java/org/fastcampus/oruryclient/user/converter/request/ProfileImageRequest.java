package org.fastcampus.oruryclient.user.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.user.dto.UserDto;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProfileImageRequest(
        String profileImage
) {
    public static UserDto toDto(UserDto userDto, String profileImage) {
        return UserDto.of(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                userDto.password(),
                userDto.signUpType(),
                userDto.gender(),
                userDto.birthday(),
                profileImage,
                userDto.createdAt(),
                null
        );
    }
}
