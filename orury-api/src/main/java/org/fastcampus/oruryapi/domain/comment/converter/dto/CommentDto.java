package org.fastcampus.oruryapi.domain.comment.converter.dto;

import org.fastcampus.oruryapi.domain.comment.db.model.Comment;
import org.fastcampus.oruryapi.domain.post.db.model.Post;
import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.time.LocalDateTime;

/**
 * DTO for {@link Comment}
 */
public record CommentDto(
        Long id,
        String content,
        Long parentId,
        Post post,
        User user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentDto of(
            Long id,
            String content,
            Long parentId,
            Post post,
            User user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CommentDto(
                id,
                content,
                parentId,
                post,
                user,
                createdAt,
                updatedAt
        );
    }
}
