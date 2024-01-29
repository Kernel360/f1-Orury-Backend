package org.fastcampus.oruryclient.review.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.fastcampus.oruryclient.global.constants.NumberConstants;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewsWithCursorResponse(
        List<ReviewsResponse> reviews,
        Long cursor,
        String gymName
) {
    public static ReviewsWithCursorResponse of(List<ReviewsResponse> reviews, String gymName) {
        Long cursor = (reviews.isEmpty())
                ? NumberConstants.LAST_CURSOR
                : reviews.get(reviews.size() - 1).id();

        return new ReviewsWithCursorResponse(reviews, cursor, gymName);
    }

}
