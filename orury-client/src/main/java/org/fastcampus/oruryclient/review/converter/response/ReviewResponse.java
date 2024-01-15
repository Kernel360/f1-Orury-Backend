package org.fastcampus.oruryclient.review.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewResponse(
        Long id,
        String content,
        List<String> images,
        float score,
        boolean isMine
) {
    public static ReviewResponse of(ReviewDto reviewDto, Long userId) {
        List<String> imagesAsList = ImageUrlConverter.convertToList(reviewDto.images());
        boolean isMine = reviewDto.userDto().id().equals(userId);

        return new ReviewResponse(
                reviewDto.id(),
                reviewDto.content(),
                imagesAsList,
                reviewDto.score(),
                isMine
        );
    }

}
