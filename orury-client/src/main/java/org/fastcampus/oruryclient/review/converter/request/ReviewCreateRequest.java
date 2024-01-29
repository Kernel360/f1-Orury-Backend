package org.fastcampus.oruryclient.review.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
                "",
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
