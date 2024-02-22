package org.fastcampus.oruryclient.comment.converter.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.fastcampus.orurydomain.comment.dto.CommentDto;

import org.fastcampus.orurydomain.comment.dto.CommentDto;
import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        Long parentId,
        int likeCount,
        boolean isMine,
        Long userId,
        String userNickname,
        String userProfileImage,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
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
                commentDto.userDto().id(),
                commentDto.userDto().nickname(),
                commentDto.userDto().profileImage(),
                commentDto.createdAt(),
                commentDto.updatedAt(),
                (commentDto.deleted() == 1) ? false : isLike,
                commentDto.deleted()
        );
    }
}
