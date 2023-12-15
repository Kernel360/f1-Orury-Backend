package org.fastcampus.oruryapi.domain.comment.db.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "COMMENT_LIKE")
public class CommentLike {
    @EmbeddedId
    private CommentLikePK commentLikePK;

    private CommentLike(CommentLikePK commentLikePK) {
        this.commentLikePK = commentLikePK;
    }

    public static CommentLike of(CommentLikePK commentLikePK) {
        return new CommentLike(commentLikePK);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentLike commentLike)) return false;
        return Objects.equals(commentLikePK, commentLike.commentLikePK);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commentLikePK);
    }
}
