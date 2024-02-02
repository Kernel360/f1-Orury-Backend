package org.fastcampus.oruryclient.comment.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

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
                0,
                postDto,
                userDto,
                NumberConstants.IS_NOT_DELETED,
                null,
                null
        );
    }
}
