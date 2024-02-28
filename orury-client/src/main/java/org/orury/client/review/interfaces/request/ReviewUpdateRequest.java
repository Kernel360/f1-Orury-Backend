package org.orury.client.review.converter.request;

import org.orury.domain.review.domain.dto.ReviewDto;

public record ReviewUpdateRequest(
        String content,
        float score
) {
    public static ReviewUpdateRequest of(
            String content,
            float score
    ) {
        return new ReviewUpdateRequest(
                content,
                score
        );
    }

    public ReviewDto toDto(ReviewDto reviewDto) {

        return ReviewDto.of(
                reviewDto.id(),
                content,
                reviewDto.images(),
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
