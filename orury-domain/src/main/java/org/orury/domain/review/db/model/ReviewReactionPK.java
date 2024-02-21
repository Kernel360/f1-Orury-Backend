package org.orury.domain.review.db.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ReviewReactionPK implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    private ReviewReactionPK(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }

    public static ReviewReactionPK of(Long userId, Long reviewId) {
        return new ReviewReactionPK(
                userId,
                reviewId
        );
    }


}
