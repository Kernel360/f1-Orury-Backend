package org.fastcampus.oruryclient.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
