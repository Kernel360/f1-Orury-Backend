package org.fastcampus.oruryapi.domain.post.converter.response;

import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;

import java.time.LocalDateTime;

public record PostsResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        String thumbnailImage,
        int category,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostsResponse of(PostDto postDto) {

        return new PostsResponse(
                postDto.id(),
                postDto.title(),
                postDto.content(),
                postDto.viewCount(),
                postDto.commentCount(),
                postDto.likeCount(),
                postDto.images(),
                postDto.category(),
                postDto.userDto().id(),
                postDto.userDto().nickname(),
                postDto.userDto().profileImage(),
                postDto.createdAt(),
                postDto.updatedAt()
        );
    }
}
