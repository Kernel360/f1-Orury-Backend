package org.fastcampus.oruryclient.review.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.util.image.converter.ImageUrlConverter;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewGetResponse(
        Long id,
        String content,
        List<String> images,
        float score
) {
    public static ReviewGetResponse of(ReviewDto reviewDto) {
        List<String> imagesAsList = ImageUrlConverter.convertToList(reviewDto.images());

        return new ReviewGetResponse(
                reviewDto.id(),
                reviewDto.content(),
                imagesAsList,
                reviewDto.score()
        );
    }

}
