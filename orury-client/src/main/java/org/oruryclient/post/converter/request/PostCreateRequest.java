package org.oruryclient.post.converter.request;

import org.orurydomain.post.dto.PostDto;
import org.orurydomain.user.dto.UserDto;

import java.util.List;

public record PostCreateRequest(
        String title,
        String content,
        int category
) {
    public static PostCreateRequest of(String title, String content, int category) {
        return new PostCreateRequest(title, content, category);
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
}
