package org.fastcampus.oruryclient.post.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        List<String> images,
        int category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine,
        Long userId,
        String userNickname,
        String userProfileImage,
        boolean isLike
) {
    public static PostResponse of(PostDto postDto, UserDto userDto, boolean isLike) {
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
                isLike
        );
    }

    private static boolean mine(PostDto postDto, UserDto userDto) {
        return postDto.userDto()
                .id()
                .equals(userDto.id());
    }
}
