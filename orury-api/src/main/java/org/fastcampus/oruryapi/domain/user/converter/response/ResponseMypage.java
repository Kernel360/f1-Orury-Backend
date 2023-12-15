package org.fastcampus.oruryapi.domain.user.converter.response;

import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.time.LocalDate;
import java.util.Objects;

public record ResponseMypage(
        Long id,
        String email,
        String nickname,
        int signUpType,
        int gender,
        LocalDate birthday,
        String profileImage
){
    public static ResponseMypage toDto(UserDto userDto){
        return new ResponseMypage(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                userDto.signUpType(),
                userDto.gender(),
                userDto.birthday(),
                userDto.profileImage()
        );
    }
}
