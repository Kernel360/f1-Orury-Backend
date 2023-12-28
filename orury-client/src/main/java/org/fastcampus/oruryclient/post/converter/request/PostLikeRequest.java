package org.fastcampus.oruryclient.post.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.post.dto.PostLikePKDto;
import org.fastcampus.orurydomain.post.db.model.PostLikePK;
import org.fastcampus.orurydomain.user.dto.UserDto;

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
