package org.orury.client.user.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.client.global.IdIdentifiable;
import org.orury.domain.post.domain.dto.PostDto;

import java.time.LocalDateTime;
import java.util.Objects;

public record MyPostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        String thumbnailImage,
        int category,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt
) implements IdIdentifiable {
    public static MyPostResponse of(PostDto postDto) {
        return new MyPostResponse(
                postDto.id(),
                postDto.title(),
                postDto.content(),
                postDto.viewCount(),
                postDto.commentCount(),
                postDto.likeCount(),
                (Objects.isNull(postDto.images()) || postDto.images().isEmpty()) ? null : postDto.images().get(0),
                postDto.category(),
                postDto.createdAt(),
                postDto.updatedAt()
        );
    }
}
