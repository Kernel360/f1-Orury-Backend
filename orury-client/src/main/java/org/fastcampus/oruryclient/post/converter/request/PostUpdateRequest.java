package org.fastcampus.oruryclient.domain.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryclient.domain.user.converter.dto.UserDto;

@Slf4j
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
                postDto.commentCount(),
                postDto.likeCount(),
                images,
                category,
                userDto,
                postDto.createdAt(),
                null
        );
    }
}
