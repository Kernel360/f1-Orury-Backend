package org.fastcampus.oruryapi.domain.user.converter.response;

import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.time.LocalDate;
import java.util.Objects;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        int signupType,
        int gender,
        LocalDate birthday,
        String profileImage
){
    public static UserResponse of(
            Long id,
            String email,
            String nickname,
            int signupType,
            int gender,
            LocalDate birthday,
            String profileImage
    ){
        return new UserResponse(
                id,
                email,
                nickname,
                signupType,
                gender,
                birthday,
                profileImage
        );
    }

    public static UserResponse from(User entity){
        return UserResponse.of(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getSignupType(),
                entity.getGender(),
                entity.getBirthday(),
                entity.getProfileImage()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponse that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
