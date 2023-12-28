package org.fastcampus.oruryclient.domain.user.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.domain.user.converter.dto.UserDto;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProfileImageRequest(
        Long id,
        String profileImage
){


    public static UserDto toDto(UserDto userDto, ProfileImageRequest profileImageRequest){
        return UserDto.of(
                profileImageRequest.id(),
                userDto.email(),
                userDto.nickname(),
                userDto.password(),
                userDto.signUpType(),
                userDto.gender(),
                userDto.birthday(),
                profileImageRequest.profileImage(),
                userDto.createdAt(),
                null
        );
    }
}
