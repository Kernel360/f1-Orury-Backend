package org.fastcampus.oruryapi.domain.comment.db.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CommentLikePK implements Serializable {
    private Long userId;
    private Long commentId;

    private CommentLikePK(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public CommentLikePK of(User user, Comment comment) {
        return new CommentLikePK(
                user.getId(),
                comment.getId()
        );
    }
}
