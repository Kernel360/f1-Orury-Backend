package org.fastcampus.oruryapi.domain.comment.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.comment.converter.dto.CommentDto;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.global.constants.NumberConstants;

@Slf4j
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentCreateRequest(
        String content,
        Long parentId,
        Long postId
) {
    public static CommentCreateRequest of(String content, Long parentId, Long postId) {
        return new CommentCreateRequest(content, parentId, postId);
    }

    public CommentDto toDto(UserDto userDto, PostDto postDto) {
        return CommentDto.of(
                null,
                content,
                parentId,
                postDto,
                userDto,
                NumberConstants.IS_NOT_DELETED,
                null,
                null
        );
    }
}
