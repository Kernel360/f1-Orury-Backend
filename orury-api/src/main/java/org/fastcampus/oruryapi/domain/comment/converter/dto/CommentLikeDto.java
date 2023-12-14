package org.fastcampus.oruryapi.domain.comment.converter.dto;

import org.fastcampus.oruryapi.domain.comment.db.model.Comment;
import org.fastcampus.oruryapi.domain.comment.db.model.CommentLike;
import org.fastcampus.oruryapi.domain.comment.db.model.CommentLikePK;
import org.fastcampus.oruryapi.domain.user.db.model.User;

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
