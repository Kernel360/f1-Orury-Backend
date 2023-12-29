package org.fastcampus.oruryclient.comment.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.global.constants.Constants;
import org.fastcampus.orurydomain.comment.dto.CommentDto;

import java.time.LocalDateTime;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentResponse(
        Long id,
        String content,
        Long parentId,
        int likeCount,
        boolean isMine,
        String userNickname,
        String userProfileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isLike,
        int deleted
) {
    public static CommentResponse of(CommentDto commentDto, Long userId, boolean isLike) {
        return new CommentResponse(
                commentDto.id(),
                commentDto.content(),
                commentDto.parentId(),
                commentDto.likeCount(),
                (commentDto.deleted() == 1) ? false : commentDto.userDto().id().equals(userId),
                (commentDto.deleted() == 1) ? Constants.DELETED_USER.getMessage() : commentDto.userDto().nickname(),
                (commentDto.deleted() == 1) ? Constants.DELETED_USER.getMessage() : commentDto.userDto().profileImage(),
                commentDto.createdAt(),
                commentDto.updatedAt(),
                (commentDto.deleted() == 1) ? false : isLike,
                commentDto.deleted()
        );
    }
}
