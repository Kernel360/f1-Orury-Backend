package org.fastcampus.oruryclient.user.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.global.IdIdentifiable;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
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
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements IdIdentifiable {
    public static MyReviewResponse of(ReviewDto reviewDto) {
        List<String> imagesAsList = ImageUrlConverter.convertStringToList(reviewDto.images());

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
                imagesAsList,
                reviewDto.score(),
                reviewReactionCount,
                reviewDto.createdAt(),
                reviewDto.updatedAt()
        );
    }


    private static String mapReactionType(int reaction) {
        switch (reaction) {
            case NumberConstants.THUMB_REACTION:
                return "thumb";
            case NumberConstants.INTERREST_REACTION:
                return "interest";
            case NumberConstants.HELP_REACTION:
                return "help";
            case NumberConstants.LIKE_REACTION:
                return "like";
            case NumberConstants.ANGRY_REACTION:
                return "angry";
            default:
                return null;
        }
    }

    private static record ReviewReactionCount(String type, int count) {
    }
}
