package org.fastcampus.oruryclient.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
