package org.fastcampus.oruryapi.domain.post.converter.response;

import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;

import java.time.LocalDateTime;

public record PostsResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        String thumbnailImage,
        int category,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int likeCount,
        int commentCount
) {
    public static PostsResponse of(PostDto postDto, int likeCount, int commentCount) {

        return new PostsResponse(
                postDto.id(),
                postDto.title(),
                postDto.content(),
                postDto.viewCount(),
                postDto.images(),
                postDto.category(),
                postDto.userDto().id(),
                postDto.userDto().nickname(),
                postDto.userDto().profileImage(),
                postDto.createdAt(),
                postDto.updatedAt(),
                likeCount,
                commentCount
        );
    }
}
