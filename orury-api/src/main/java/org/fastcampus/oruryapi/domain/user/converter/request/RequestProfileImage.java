package org.fastcampus.oruryapi.domain.user.converter.request;

import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.model.User;


public record RequestProfileImage (
        Long id,
        String profileImage
){

    public static UserDto toDto(User user, RequestProfileImage requestProfileImage){
        return UserDto.of(
                requestProfileImage.id(),
                user.getEmail(),
                user.getNickname(),
                user.getPassword(),
                user.getSignUpType(),
                user.getGender(),
                user.getBirthday(),
                requestProfileImage.profileImage(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
