package org.oruryclient.user.converter.response;

import org.oruryclient.global.IdIdentifiable;
import org.orurydomain.comment.dto.CommentDto;

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
