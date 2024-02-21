package org.orury.client.user.converter.response;

import org.orury.client.global.IdIdentifiable;
import org.orury.domain.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

public record MyReviewResponse(
        Long id,
        String content,
        List<String> images,
        float score,
        List<MyReviewResponse.ReviewReactionCount> reviewReactionCount,
        LocalDateTime createdAt,
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
