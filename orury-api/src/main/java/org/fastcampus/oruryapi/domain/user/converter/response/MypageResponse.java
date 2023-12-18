package org.fastcampus.oruryapi.domain.user.converter.response;

import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

import java.time.LocalDate;

public record MypageResponse(
        Long id,
        String email,
        String nickname,
        int signUpType,
        int gender,
        LocalDate birthday,
        String profileImage
){
    public static MypageResponse toDto(UserDto userDto){
        return new MypageResponse(
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
