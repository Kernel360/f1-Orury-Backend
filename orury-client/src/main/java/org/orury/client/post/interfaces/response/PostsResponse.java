package org.orury.client.post.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.domain.post.domain.dto.PostDto;

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
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
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
                (postDto.images().isEmpty()) ? null : postDto.images().get(0),
                postDto.category(),
                postDto.userDto().id(),
                postDto.userDto().nickname(),
                postDto.userDto().profileImage(),
                postDto.createdAt(),
                postDto.updatedAt()
        );
    }
}
