package org.fastcampus.oruryapi.domain.user.converter.dto;

import org.fastcampus.oruryapi.domain.user.db.model.User;

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
        int signupType,
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
            int signupType,
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
                signupType,
                gender,
                birthday,
                profileImage,
                createdAt,
                updatedAt
        );
    }

    public static UserDto from(User entity){
        return new


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
>>>>>>> upstream/develop
        return UserDto.of(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
<<<<<<< HEAD
                entity.getPassword(),
                entity.getSignupType(),
                entity.getGender(),
                entity.getBirthday(),
                entity.getProfileImage(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public User toEntity(){
        return User.of(
                this
        );
    }

=======
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
>>>>>>> upstream/develop
}
