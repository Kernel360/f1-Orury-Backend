package org.fastcampus.oruryclient.user.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.fastcampus.oruryclient.global.IdIdentifiable;
import org.fastcampus.orurydomain.post.dto.PostDto;

import java.time.LocalDateTime;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MyPostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        String thumbnailImage,
        int category,
        LocalDateTime createdAt,
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
                postDto.images(),
                postDto.category(),
                postDto.createdAt(),
                postDto.updatedAt()
        );
    }
}
