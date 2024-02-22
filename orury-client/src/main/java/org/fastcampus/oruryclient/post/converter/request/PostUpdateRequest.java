package org.fastcampus.oruryclient.post.converter.request;

import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

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
