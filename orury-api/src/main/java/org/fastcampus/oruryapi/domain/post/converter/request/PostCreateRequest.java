package org.fastcampus.oruryapi.domain.post.converter.request;

import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

@Slf4j
public record PostCreateRequest(
        String title,
        String content,
        String images,
        int category,
        Long userId
) {
    public static PostCreateRequest of(String title, String content, String images, int category, Long userId) {
        return new PostCreateRequest(title, content, images, category, userId);
    }

    public PostDto toDto(UserDto userDto) {
        return PostDto.of(
                null,
                title,
                content,
                0,
                images,
                category,
                userDto,
                null,
                null
        );
    }
}
