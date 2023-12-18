package org.fastcampus.oruryapi.domain.user.converter.request;

import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.model.User;

public record UserInfoRequest(
        Long id,
        String nickname
) {
    public static UserDto toDto(User user, UserInfoRequest userInfoRequest){
        return UserDto.of(
                userInfoRequest.id(),
                user.getEmail(),
                userInfoRequest.nickname(),
                user.getPassword(),
                user.getSignUpType(),
                user.getGender(),
                user.getBirthday(),
                user.getProfileImage(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
