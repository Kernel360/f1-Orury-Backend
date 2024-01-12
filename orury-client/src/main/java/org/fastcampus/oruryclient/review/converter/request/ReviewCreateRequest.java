package org.fastcampus.oruryclient.review.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewCreateRequest(
        String content,
        List<String> images,
        float score,
        Long gymId
) {
    public static ReviewCreateRequest of(String content, List<String> images, float score, Long gymId) {
        return new ReviewCreateRequest(content, images, score, gymId);
    }

    public ReviewDto toDto(UserDto userDto, GymDto gymDto) {
        String imagesAsString = ImageUrlConverter.convertToString(images);

        return ReviewDto.of(
                null,
                content,
                imagesAsString,
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
