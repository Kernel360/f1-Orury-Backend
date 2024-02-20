package org.oruryclient.user.converter.response;

import org.oruryclient.global.IdIdentifiable;
import org.orurydomain.post.dto.PostDto;

import java.time.LocalDateTime;

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
                (postDto.images()
                        .isEmpty()) ? null : postDto.images()
                        .get(0),
                postDto.category(),
                postDto.createdAt(),
                postDto.updatedAt()
        );
    }
}
