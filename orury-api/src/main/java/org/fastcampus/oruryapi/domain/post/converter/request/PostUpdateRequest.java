package org.fastcampus.oruryapi.domain.post.converter.request;

import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

@Slf4j
public record PostUpdateRequest(
        Long id,
        String title,
        String content,
        String images,
        int category,
        Long userId
) {
    public static PostUpdateRequest of(Long id, String title, String content, String images, int category, Long userId) {
        return new PostUpdateRequest(id, title, content, images, category, userId);
    }

    public PostDto toDto(PostDto postDto, UserDto userDto) {
        return PostDto.of(
                postDto.id(),
                    title,
                    content,
                    postDto.viewCount(),
                    images,
                    category,
                    userDto,
                    postDto.createdAt(),
                    null
        );
    }
}
