package org.fastcampus.oruryclient.review.util;

import lombok.Getter;

@Getter
public enum ReviewMessage {
    REVIEW_CREATED("Review Created"),
    REVIEW_READ("Review Read"),
    REVIEWS_READ("Reviews Read"),
    REVIEW_UPDATED("Review Updated"),
    REVIEW_DELETED("Review Deleted"),
    REVIEW_REACTION_CREATED("Review Reaction Created"),
    REVIEW_REACTION_DELETED("Review Reaction Deleted");

    private final String message;

    ReviewMessage(String message) {
        this.message = message;
    }
}
