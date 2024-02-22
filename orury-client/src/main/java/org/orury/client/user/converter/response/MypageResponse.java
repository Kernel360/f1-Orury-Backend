package org.orury.client.user.converter.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.domain.user.dto.UserDto;

import java.time.LocalDate;

public record MypageResponse(
        Long id,
        String email,
        String nickname,
        int signUpType,
        int gender,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDate birthday,
        String profileImage
) {
    public static MypageResponse of(UserDto userDto) {
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
