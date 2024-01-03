package org.fastcampus.orurydomain.comment.dto;

import org.fastcampus.orurydomain.comment.db.model.CommentLike;
import org.fastcampus.orurydomain.comment.db.model.CommentLikePK;

/**
 * DTO for {@link CommentLike}
 */
public record CommentLikeDto(
        CommentLikePK commentLikePK
) {
    private static CommentLikeDto of(
            CommentLikePK commentLikePK
    ) {
        return new CommentLikeDto(
                commentLikePK
        );
    }

    public static CommentLikeDto from(CommentLike entity) {
        return CommentLikeDto.of(
                entity.getCommentLikePK()
        );
    }

    public CommentLike toEntity() {
        return CommentLike.of(
                commentLikePK
        );
    }
}
