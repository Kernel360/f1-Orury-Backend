package org.fastcampus.oruryclient.post.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.post.dto.PostDto;

import java.time.LocalDateTime;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        String images,
        int category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine,
        String userNickname,
        String userProfileImage,
        boolean isLike
) {
    public static PostResponse of(PostDto postDto, Long userId, boolean isLike) {
        return new PostResponse(
                postDto.id(),
                postDto.title(),
                postDto.content(),
                postDto.viewCount(),
                postDto.commentCount(),
                postDto.likeCount(),
                postDto.images(),
                postDto.category(),
                postDto.createdAt(),
                postDto.updatedAt(),
                postDto.userDto().id().equals(userId),
                postDto.userDto().nickname(),
                postDto.userDto().profileImage(),
                isLike
        );
    }
}
