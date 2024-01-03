package org.fastcampus.oruryclient.user.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.user.dto.UserDto;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfoRequest(
        Long id,
        String nickname
) {
    public static UserDto toDto(UserDto userDto, UserInfoRequest userInfoRequest){
        return UserDto.of(
                userInfoRequest.id(),
                userDto.email(),
                userInfoRequest.nickname(),
                userDto.password(),
                userDto.signUpType(),
                userDto.gender(),
                userDto.birthday(),
                userDto.profileImage(),
                userDto.createdAt(),
                null
        );
    }
}
