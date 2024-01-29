package org.fastcampus.orurydomain.user.dto;

import org.fastcampus.orurydomain.user.db.model.User;

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
        LocalDateTime updatedAt
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
            LocalDateTime updatedAt
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
                updatedAt
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
                entity.getUpdatedAt()
        );
    }

    public static UserDto from(User entity, String imageUrl) {
        return UserDto.of(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getPassword(),
                entity.getSignUpType(),
                entity.getGender(),
                entity.getBirthday(),
                imageUrl,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
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
                updatedAt
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
                updatedAt
        );
    }
}