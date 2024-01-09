package org.fastcampus.oruryclient.review.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.util.image.converter.ImageUrlConverter;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewsResponse(
        Long id,
        String content,
        List<String> images,
        float score,
        int interestCount,
        int likeCount,
        int helpCount,
        int thumbCount,
        int angryCount,
        Long userId,
        String userNickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine

) {
    public static ReviewsResponse of(ReviewDto reviewDto, Long userId) {
        List<String> imagesAsList = ImageUrlConverter.convertToList(reviewDto.images());
        boolean isMine = reviewDto.userDto().id().equals(userId);

        return new ReviewsResponse(
                reviewDto.id(),
                reviewDto.content(),
                imagesAsList,
                reviewDto.score(),
                reviewDto.interestCount(),
                reviewDto.likeCount(),
                reviewDto.helpCount(),
                reviewDto.thumbCount(),
                reviewDto.angryCount(),
                reviewDto.userDto().id(),
                reviewDto.userDto().nickname(),
                reviewDto.createdAt(),
                reviewDto.updatedAt(),
                isMine
        );
    }
}
