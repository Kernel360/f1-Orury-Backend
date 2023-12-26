package org.fastcampus.oruryapi.domain.comment.converter.dto;

import org.fastcampus.oruryapi.domain.comment.db.model.CommentLikePK;

public record CommentLikePKDto(
        Long userId,
        Long commentId
) {
    private static CommentLikePKDto of(Long userId, Long commentId) {
        return new CommentLikePKDto(
                userId,
                commentId
        );
    }

    public static CommentLikePKDto from(CommentLikePK entity) {
        return CommentLikePKDto.of(
                entity.getUserId(),
                entity.getCommentId()
        );
    }

    public CommentLikePK toEntity() {
        return CommentLikePK.of(
                userId,
                commentId
        );
    }
}
