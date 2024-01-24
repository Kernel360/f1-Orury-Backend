package org.fastcampus.oruryclient.review.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewUpdateRequest(
        String content,
        List<String> images,
        float score
) {
    public static ReviewUpdateRequest of(
            String content,
            List<String> images,
            float score
    ) {
        return new ReviewUpdateRequest(
                content,
                images,
                score
        );
    }

    public ReviewDto toDto(ReviewDto reviewDto) {
        String imagesAsString = ImageUrlConverter.convertListToString(images);

        return ReviewDto.of(
                reviewDto.id(),
                content,
                imagesAsString,
                score,
                reviewDto.interestCount(),
                reviewDto.likeCount(),
                reviewDto.helpCount(),
                reviewDto.thumbCount(),
                reviewDto.angryCount(),
                reviewDto.userDto(),
                reviewDto.gymDto(),
                reviewDto.createdAt(),
                null
        );
    }

}
