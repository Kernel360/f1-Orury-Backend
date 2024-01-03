package org.fastcampus.orurydomain.comment.dto;

import org.fastcampus.orurydomain.comment.db.model.Comment;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link Comment}
 */
public record CommentDto(
        Long id,
        String content,
        Long parentId,
        int likeCount,
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
            int likeCount,
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
                likeCount,
                postDto,
                userDto,
                deleted,
                createdAt,
                updatedAt
        );
    }

    public static CommentDto from(Comment entity) {
        return CommentDto.of(
                entity.getId(),
                entity.getContent(),
                entity.getParentId(),
                entity.getLikeCount(),
                PostDto.from(entity.getPost()),
                UserDto.from(entity.getUser()),
                entity.getDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Comment toEntity() {
        return Comment.of(
                id,
                content,
                parentId,
                likeCount,
                postDto.toEntity(),
                userDto.toEntity(),
                deleted,
                createdAt,
                updatedAt
        );
    }
}
