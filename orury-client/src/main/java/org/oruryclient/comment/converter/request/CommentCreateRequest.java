package org.oruryclient.comment.converter.request;

import org.orurydomain.comment.dto.CommentDto;
import org.orurydomain.global.constants.NumberConstants;
import org.orurydomain.post.dto.PostDto;
import org.orurydomain.user.dto.UserDto;

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
