package org.fastcampus.oruryapi.domain.user.converter.response;

import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.time.LocalDate;
import java.util.Objects;

public record ResponseMypage(
        Long id,
        String email,
        String nickname,
        int signupType,
        int gender,
        LocalDate birthday,
        String profileImage
){
    public static ResponseMypage of(
            Long id,
            String email,
            String nickname,
            int signupType,
            int gender,
            LocalDate birthday,
            String profileImage
    ){
        return new ResponseMypage(
                id,
                email,
                nickname,
                signupType,
                gender,
                birthday,
                profileImage
        );
    }

    public static ResponseMypage from(User entity){
        return ResponseMypage.of(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getSignupType(),
                entity.getGender(),
                entity.getBirthday(),
                entity.getProfileImage()
        );
    }
}
