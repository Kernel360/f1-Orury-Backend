package org.orury.client.review.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.review.domain.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewsResponse(
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

) {
    public static ReviewsResponse of(ReviewDto reviewDto, Long loginId, int myReaction) {

        boolean isMine = reviewDto.userDto()
                .id()
                .equals(loginId);

        List<ReviewReactionCount> reviewReactionCount = List.of(
                new ReviewReactionCount("thumb", reviewDto.thumbCount()),
                new ReviewReactionCount("interest", reviewDto.interestCount()),
                new ReviewReactionCount("help", reviewDto.helpCount()),
                new ReviewReactionCount("like", reviewDto.likeCount()),
                new ReviewReactionCount("angry", reviewDto.angryCount())
        );

        Writer writer = new Writer(reviewDto.userDto()
                .id(), reviewDto.userDto()
                .nickname(),
                reviewDto.userDto().profileImage()
        );

        String myReactionType = mapReactionType(myReaction);

        return new ReviewsResponse(
                reviewDto.id(),
                reviewDto.content(),
                reviewDto.images(),
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
