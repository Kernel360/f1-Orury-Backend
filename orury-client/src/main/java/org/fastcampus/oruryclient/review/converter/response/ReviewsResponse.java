package org.fastcampus.oruryclient.review.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewsResponse(
        Long id,
        String content,
        List<String> images,
        float score,
        List<ReviewReactionCount> reviewReactionCount,
        String myReaction,
        Writer writer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine

) {
    public static ReviewsResponse of(ReviewDto reviewDto, Long userId, int myReaction) {
        List<String> imagesAsList = ImageUrlConverter.convertToList(reviewDto.images());

        boolean isMine = reviewDto.userDto().id().equals(userId);

        List<ReviewReactionCount> reviewReactionCount = List.of(
                new ReviewReactionCount("thumb", reviewDto.thumbCount()),
                new ReviewReactionCount("interest", reviewDto.interestCount()),
                new ReviewReactionCount("help", reviewDto.helpCount()),
                new ReviewReactionCount("like", reviewDto.likeCount()),
                new ReviewReactionCount("angry", reviewDto.angryCount())
        );

        Writer writer = new Writer(reviewDto.userDto().id(), reviewDto.userDto().nickname(), reviewDto.userDto().profileImage());


        String myReactionType = mapReactionType(myReaction);

        return new ReviewsResponse(
                reviewDto.id(),
                reviewDto.content(),
                imagesAsList,
                reviewDto.score(),
                reviewReactionCount,
                myReactionType,
                writer,
                reviewDto.createdAt(),
                reviewDto.updatedAt(),
                isMine
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

    private static record Writer(Long id, String nickname, String profileImage) {
    }
}
