package org.orury.client.user.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.client.global.IdIdentifiable;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.review.domain.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

public record MyReviewResponse(
        Long id,
        String content,
        List<String> images,
        float score,
        List<ReviewReactionCount> reviewReactionCount,
        String myReaction,
        Writer writer,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt,
        boolean isMine
) implements IdIdentifiable {
    public static MyReviewResponse of(ReviewDto reviewDto, int myReaction) {

        List<MyReviewResponse.ReviewReactionCount> reviewReactionCount = List.of(
                new MyReviewResponse.ReviewReactionCount("thumb", reviewDto.thumbCount()),
                new MyReviewResponse.ReviewReactionCount("interest", reviewDto.interestCount()),
                new MyReviewResponse.ReviewReactionCount("help", reviewDto.helpCount()),
                new MyReviewResponse.ReviewReactionCount("like", reviewDto.likeCount()),
                new MyReviewResponse.ReviewReactionCount("angry", reviewDto.angryCount())
        );

        String myReactionType = mapReactionType(myReaction);

        Writer writer = new Writer(reviewDto.userDto()
                .id(), reviewDto.userDto()
                .nickname(),
                reviewDto.userDto().profileImage()
        );

        return new MyReviewResponse(
                reviewDto.id(),
                reviewDto.content(),
                reviewDto.images(),
                reviewDto.score(),
                reviewReactionCount,
                myReactionType,
                writer,
                reviewDto.createdAt(),
                reviewDto.updatedAt(),
                true
        );
    }

    private static String mapReactionType(int reaction) {
        return switch (reaction) {
            case NumberConstants.THUMB_REACTION -> "thumb";
            case NumberConstants.INTERREST_REACTION -> "interest";
            case NumberConstants.HELP_REACTION -> "help";
            case NumberConstants.LIKE_REACTION -> "like";
            case NumberConstants.ANGRY_REACTION -> "angry";
            default -> null;
        };
    }

    private record ReviewReactionCount(String type, int count) {
    }

    private record Writer(Long id, String nickname, String profileImage) {
    }
}
