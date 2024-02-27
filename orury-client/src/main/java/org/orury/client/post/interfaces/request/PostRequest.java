package org.orury.client.post.interfaces.request;

import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.user.dto.UserDto;

import java.util.List;

public record PostRequest(
        Long id,
        String title,
        String content,
        int category
) {
    public static PostRequest of(String title, String content, int category) {
        return PostRequest.of(null, title, content, category);
    }

    public static PostRequest of(Long id, String title, String content, int category) {
        return new PostRequest(id, title, content, category);
    }

    public PostDto toDto(UserDto userDto) {
        return PostDto.of(
                null,
                title,
                content,
                0,
                0,
                0,
                List.of(),
                category,
                userDto,
                null,
                null
        );
    }

    public PostDto toDto(PostDto postDto, UserDto userDto) {
        return PostDto.of(
                postDto.id(),
                title,
                content,
                postDto.viewCount(),
                postDto.commentCount(),
                postDto.likeCount(),
                postDto.images(),
                category,
                userDto,
                postDto.createdAt(),
                null
        );
    }
}
