package org.orury.domain.user.domain.dto;

import org.orury.domain.global.constants.Constants;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link User}
 */
public record UserDto(
        Long id,
        String email,
        String nickname,
        String password,
        int signUpType,
        int gender,
        LocalDate birthday,
        String profileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int isDeleted
) {
    public static UserDto of(
            Long id,
            String email,
            String nickname,
            String password,
            int signUpType,
            int gender,
            LocalDate birthday,
            String profileImage,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int isDeleted
    ) {
        return new UserDto(
                id,
                email,
                nickname,
                password,
                signUpType,
                gender,
                birthday,
                profileImage,
                createdAt,
                updatedAt,
                isDeleted
        );
    }

    public static UserDto from(User entity) {
        return UserDto.of(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getPassword(),
                entity.getSignUpType(),
                entity.getGender(),
                entity.getBirthday(),
                entity.getProfileImage(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getIsDeleted()
        );
    }

    public static UserDto from(User entity, String imageUrl) {
        return UserDto.of(
                entity.getId(),
                entity.getEmail(),
                (entity.getIsDeleted() == NumberConstants.IS_DELETED) ? Constants.DELETED_USER_NICKNAME.getMessage() : entity.getNickname(),
                entity.getPassword(),
                entity.getSignUpType(),
                entity.getGender(),
                entity.getBirthday(),
                imageUrl,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getIsDeleted()
        );
    }

    public User toEntity() {
        return User.of(
                id,
                email,
                nickname,
                password,
                signUpType,
                gender,
                birthday,
                profileImage,
                createdAt,
                updatedAt,
                isDeleted
        );
    }

    public User toEntity(String newProfileImage) {
        return User.of(
                id,
                email,
                nickname,
                password,
                signUpType,
                gender,
                birthday,
                newProfileImage,
                createdAt,
                updatedAt,
                isDeleted
        );
    }
}