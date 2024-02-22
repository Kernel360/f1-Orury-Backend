package org.orury.client.post.converter.request;

import org.orury.domain.post.dto.PostDto;
import org.orury.domain.user.dto.UserDto;

public record PostUpdateRequest(
        Long id,
        String title,
        String content,
        int category
) {
    public static PostUpdateRequest of(Long id, String title, String content, int category) {
        return new PostUpdateRequest(id, title, content, category);
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
