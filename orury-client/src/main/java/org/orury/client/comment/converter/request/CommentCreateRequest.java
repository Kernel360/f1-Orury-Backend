package org.orury.client.comment.converter.request;

import org.orury.domain.comment.dto.CommentDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.dto.PostDto;
import org.orury.domain.user.dto.UserDto;

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
