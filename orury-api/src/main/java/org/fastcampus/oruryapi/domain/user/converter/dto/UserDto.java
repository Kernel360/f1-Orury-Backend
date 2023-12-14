package org.fastcampus.oruryapi.domain.user.converter.dto;

import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    ){
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
        return UserDto.of(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
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

}
