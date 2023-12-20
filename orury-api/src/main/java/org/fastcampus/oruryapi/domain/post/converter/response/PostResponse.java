package org.fastcampus.oruryapi.domain.post.converter.response;

import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        String images,
        int category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId,
        String userNickname,
        String userProfileImage,
        int likeCount,
        int commentCount,
        boolean isLike
) {
    public static PostResponse of(PostDto postDto, int likeCount, int commentCount, boolean isLike) {
        return new PostResponse(
                postDto.id(),
                postDto.title(),
                postDto.content(),
                postDto.viewCount(),
                postDto.images(),
                postDto.category(),
                postDto.createdAt(),
                postDto.updatedAt(),
                postDto.userDto().id(),
                postDto.userDto().nickname(),
                postDto.userDto().profileImage(),
                likeCount,
                commentCount,
                isLike
        );
    }
}
