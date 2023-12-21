package org.fastcampus.oruryapi.domain.user.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
