package org.fastcampus.oruryapi.domain.comment.converter.dto;

import org.fastcampus.oruryapi.domain.comment.db.model.Comment;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link Comment}
 */
public record CommentDto(
        Long id,
        String content,
        Long parentId,
        PostDto postDto,
        UserDto userDto,
        int deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentDto of(
            Long id,
            String content,
            Long parentId,
            PostDto postDto,
            UserDto userDto,
            int deleted,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CommentDto(
                id,
                content,
                parentId,
                postDto,
                userDto,
                deleted,
                createdAt,
                updatedAt
        );
    }
}
