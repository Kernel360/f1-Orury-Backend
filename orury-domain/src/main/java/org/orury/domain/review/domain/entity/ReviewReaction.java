package org.orury.domain.review.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"reviewReactionPK"})
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "review_reaction")
public class ReviewReaction {

    @EmbeddedId
    private ReviewReactionPK reviewReactionPK;

    @Column(name = "reaction_type", nullable = false)
    private int reactionType;

    private ReviewReaction(ReviewReactionPK reviewReactionPK, int reactionType) {
        this.reviewReactionPK = reviewReactionPK;
        this.reactionType = reactionType;
    }

    public static ReviewReaction of(ReviewReactionPK reviewReactionPK, int reactionType) {
        return new ReviewReaction(
                reviewReactionPK,
                reactionType
        );
    }
}
