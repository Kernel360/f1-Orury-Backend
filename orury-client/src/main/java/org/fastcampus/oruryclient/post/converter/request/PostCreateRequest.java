package org.fastcampus.oruryclient.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostCreateRequest(
        String title,
        String content,
        String images,
        int category
) {
    public static PostCreateRequest of(String title, String content, String images, int category) {
        return new PostCreateRequest(title, content, images, category);
    }

    public PostDto toDto(UserDto userDto) {
        return PostDto.of(
                null,
                title,
                content,
                0,
                0,
                0,
                images,
                category,
                userDto,
                null,
                null
        );
    }
}
