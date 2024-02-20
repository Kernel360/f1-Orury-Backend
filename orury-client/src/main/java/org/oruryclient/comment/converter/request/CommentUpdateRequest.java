package org.oruryclient.comment.converter.request;

import org.orurydomain.comment.dto.CommentDto;

public record CommentUpdateRequest(
        Long id,
        String content
) {
    public static CommentUpdateRequest of(Long id, String content) {
        return new CommentUpdateRequest(
                id,
                content
        );
    }

    public CommentDto toDto(CommentDto commentDto) {
        return CommentDto.of(
                commentDto.id(),
                content,
                commentDto.parentId(),
                commentDto.likeCount(),
                commentDto.postDto(),
                commentDto.userDto(),
                commentDto.deleted(),
                commentDto.createdAt(),
                null
        );
    }
}
