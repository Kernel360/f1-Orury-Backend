package org.fastcampus.oruryapi.domain.comment.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.comment.converter.dto.CommentDto;

@Slf4j
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
                commentDto.postDto(),
                commentDto.userDto(),
                commentDto.deleted(),
                commentDto.createdAt(),
                null
        );
    }
}
