package org.fastcampus.oruryclient.review.converter.request;

import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

import java.util.List;

public record ReviewCreateRequest(
        String content,
        float score,
        Long gymId
) {
    public static ReviewCreateRequest of(String content, float score, Long gymId) {
        return new ReviewCreateRequest(content, score, gymId);
    }

    public ReviewDto toDto(UserDto userDto, GymDto gymDto) {
        return ReviewDto.of(
                null,
                content,
                List.of(),
                score,
                0,
                0,
                0,
                0,
                0,
                userDto,
                gymDto,
                null,
                null
        );
    }
}
