package org.orury.client.comment.application.request;

import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.post.domain.dto.PostDto;

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
