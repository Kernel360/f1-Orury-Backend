package org.orury.client.review.interfaces.message;

import lombok.Getter;

@Getter
public enum ReviewMessage {
    REVIEW_CREATED("리뷰가 정상적으로 생성됐습니다."),
    REVIEW_READ("리뷰가 정상적으로 조회됐습니다."),
    REVIEWS_READ("리뷰 목록이 정상적으로 조회됐습니다."),
    REVIEW_UPDATED("리뷰가 정상적으로 수정됐습니다."),
    REVIEW_DELETED("리뷰가 정상적으로 삭제됐습니다."),
    REVIEW_REACTION_PROCESSED("리뷰 반응이 정상적으로 처리됐습니다."),
    ;

    private final String message;

    ReviewMessage(String message) {
        this.message = message;
    }
}
