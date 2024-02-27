package org.orury.domain.comment.domain.dto;

import org.orury.domain.comment.domain.entity.CommentLike;
import org.orury.domain.comment.domain.entity.CommentLikePK;

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
