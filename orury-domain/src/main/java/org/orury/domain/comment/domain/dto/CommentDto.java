package org.orury.domain.comment.domain.dto;

import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.post.dto.PostDto;
import org.orury.domain.user.dto.UserDto;

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

    public static CommentDto from(Comment entity, String commentUserImage) {
        return CommentDto.of(
                entity.getId(),
                entity.getContent(),
                entity.getParentId(),
                entity.getLikeCount(),
                PostDto.from(entity.getPost()),
                UserDto.from(entity.getUser(), commentUserImage),
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
