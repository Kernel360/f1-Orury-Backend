package org.orurydomain.comment.db.model;

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
@EqualsAndHashCode(of = {"commentLikePK"})
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "comment_like")
public class CommentLike {
    @EmbeddedId
    private CommentLikePK commentLikePK;

    private CommentLike(CommentLikePK commentLikePK) {
        this.commentLikePK = commentLikePK;
    }

    public static CommentLike of(CommentLikePK commentLikePK) {
        return new CommentLike(commentLikePK);
    }
}
