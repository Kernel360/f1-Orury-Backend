package org.fastcampus.oruryapi.domain.post.converter.dto;

import org.fastcampus.oruryapi.domain.post.db.model.Post;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link Post}
 */
public record PostDto(
        Long id,
        String title,
        String content,
        int viewCount,
        String images,
        int category,
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostDto of(
            Long id,
            String title,
            String content,
            int viewCount,
            String images,
            int category,
            UserDto userDto,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new PostDto(
                id,
                title,
                content,
                viewCount,
                images,
                category,
                userDto,
                createdAt,
                updatedAt
        );
    }

    public static PostDto from(Post entity) {
        return PostDto.of(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getViewCount(),
                entity.getImages(),
                entity.getCategory(),
                UserDto.from(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Post toEntity() {
        return Post.of(
                id,
                title,
                content,
                viewCount,
                images,
                category,
                userDto.toEntity(),
                createdAt,
                updatedAt
        );
    }
}
