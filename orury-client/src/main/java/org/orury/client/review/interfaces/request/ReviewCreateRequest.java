package org.orury.client.review.converter.request;

import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.domain.dto.UserDto;

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
