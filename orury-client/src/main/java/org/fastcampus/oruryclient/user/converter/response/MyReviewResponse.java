package org.fastcampus.oruryclient.user.converter.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.global.IdIdentifiable;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MyReviewResponse(
        Long id,
        String content,
        List<String> images,
        float score,
        List<MyReviewResponse.ReviewReactionCount> reviewReactionCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt,
        Long gymId,
        String gymName
) implements IdIdentifiable {
    public static MyReviewResponse of(ReviewDto reviewDto) {

        List<MyReviewResponse.ReviewReactionCount> reviewReactionCount = List.of(
                new MyReviewResponse.ReviewReactionCount("thumb", reviewDto.thumbCount()),
                new MyReviewResponse.ReviewReactionCount("interest", reviewDto.interestCount()),
                new MyReviewResponse.ReviewReactionCount("help", reviewDto.helpCount()),
                new MyReviewResponse.ReviewReactionCount("like", reviewDto.likeCount()),
                new MyReviewResponse.ReviewReactionCount("angry", reviewDto.angryCount())
        );

        return new MyReviewResponse(
                reviewDto.id(),
                reviewDto.content(),
                reviewDto.images(),
                reviewDto.score(),
                reviewReactionCount,
                reviewDto.createdAt(),
                reviewDto.updatedAt(),
                reviewDto.gymDto()
                        .id(),
                reviewDto.gymDto()
                        .name()
        );
    }

    private static record ReviewReactionCount(String type, int count) {
    }
}
