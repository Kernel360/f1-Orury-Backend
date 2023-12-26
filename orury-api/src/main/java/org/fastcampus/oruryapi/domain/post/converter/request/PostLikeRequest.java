package org.fastcampus.oruryapi.domain.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostLikePKDto;
import org.fastcampus.oruryapi.domain.post.db.model.PostLikePK;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostLikeRequest(
        Long userId,
        Long postId
) {
    public static PostLikeRequest of(Long userId, Long postId) {
        return new PostLikeRequest(userId, postId);
    }

    public PostLikePKDto toDto(UserDto userDto, PostDto postDto) {
        return PostLikePKDto.from(PostLikePK.of(userDto.id(), postDto.id()));
    }
}
