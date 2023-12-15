package org.fastcampus.oruryapi.domain.user.converter.request;

import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.model.User;

public record RequestUserInfo(
        Long id,
        String nickname
) {
    public static UserDto toDto(User user, RequestUserInfo requestUserInfo){
        return UserDto.of(
                requestUserInfo.id(),
                user.getEmail(),
                requestUserInfo.nickname(),
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
