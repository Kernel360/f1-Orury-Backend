package org.fastcampus.oruryclient.domain.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryclient.domain.user.converter.dto.UserDto;

@Slf4j
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
