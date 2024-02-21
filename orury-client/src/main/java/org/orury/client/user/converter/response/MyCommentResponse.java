package org.orury.client.user.converter.response;

import org.orury.client.global.IdIdentifiable;
import org.orury.domain.comment.dto.CommentDto;

import java.time.LocalDateTime;

public record MyCommentResponse(
        Long id,
        String content,
        Long postId,
        String postTitle,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int likeCount
) implements IdIdentifiable {
    public static MyCommentResponse of(CommentDto commentDto) {
        return new MyCommentResponse(
                commentDto.id(),
                commentDto.content(),
                commentDto.postDto().id(),
                commentDto.postDto().title(),
                commentDto.createdAt(),
                commentDto.updatedAt(),
                commentDto.likeCount()
        );
    }
}
