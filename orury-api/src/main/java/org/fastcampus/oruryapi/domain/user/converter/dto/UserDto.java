package org.fastcampus.oruryapi.domain.user.converter.dto;

import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.time.LocalDate;

/**
 * DTO for {@link User}
 */
public record UserDto(
        Long id,
        String email,
        String nickname,
        int signupType,
        int gender,
        LocalDate birthday,
        String profileImage
) {
    public static UserDto of(
            Long id,
            String email,
            String nickname,
            int signupType,
            int gender,
            LocalDate birthday,
            String profileImage
    ) {
        return new UserDto(
                id,
                email,
                nickname,
                signupType,
                gender,
                birthday,
                profileImage
        );
    }

    public static UserDto of(
            String email,
            String nickname,
            int signupType,
            int gender,
            LocalDate birthday,
            String profileImage
    ) {
        return new UserDto(
                null,
                email,
                nickname,
                signupType,
                gender,
                birthday,
                profileImage
        );
    }

    public static UserDto from(User entity) {
        return UserDto.of(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getSignupType(),
                entity.getGender(),
                entity.getBirthday(),
                entity.getProfileImage()
        );
    }

    public User toEntity() {
        return User.of(
                email,
                nickname,
                signupType,
                gender,
                birthday,
                profileImage
        );
    }
}
