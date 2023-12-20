package org.fastcampus.oruryapi.domain.post.converter.request;

import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostLikePKDto;
import org.fastcampus.oruryapi.domain.post.db.model.PostLikePK;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;

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
