package org.orury.client.post.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        List<String> images,
        int category,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime created_at,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updated_at,
        boolean isMine,
        Long userId,
        String userNickname,
        String userProfileImage,
        boolean isLike
) {
    public static PostResponse of(PostDto postDto, UserDto userDto) {
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
                mine(postDto, userDto),
                postDto.userDto().id(),
                postDto.userDto().nickname(),
                postDto.userDto().profileImage(),
                postDto.isLike()
        );
    }

    private static boolean mine(PostDto postDto, UserDto userDto) {
        return postDto.userDto().id().equals(userDto.id());
    }
}
