package org.fastcampus.oruryclient.review.converter.response;

import org.fastcampus.orurydomain.global.constants.NumberConstants;

import java.util.List;

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
