package org.fastcampus.oruryapi.domain.user.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfoRequest(
        Long id,
        String nickname
) {
    public static UserDto toDto(UserDto userdto, UserInfoRequest userInfoRequest){
        return UserDto.of(
                userInfoRequest.id(),
                userdto.email(),
                userInfoRequest.nickname(),
                userdto.password(),
                userdto.signUpType(),
                userdto.gender(),
                userdto.birthday(),
                userdto.profileImage(),
                userdto.createdAt(),
                null
        );
    }
}
