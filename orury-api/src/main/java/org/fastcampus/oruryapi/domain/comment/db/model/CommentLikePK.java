package org.fastcampus.oruryapi.domain.comment.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CommentLikePK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    private CommentLikePK(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public static CommentLikePK of(Long userId, Long commentId) {
        return new CommentLikePK(
                userId,
                commentId
        );
    }
}
