package org.fastcampus.oruryclient.post.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        List<String> images,
        int category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine,
        String userNickname,
        String userProfileImage,
        boolean isLike
) {
    public static PostResponse of(PostDto postDto, UserDto userDto, boolean isLike) {
        return new PostResponse(
                postDto.id(),
                postDto.title(),
                postDto.content(),
                postDto.viewCount(),
                postDto.commentCount(),
                postDto.likeCount(),
                ImageUrlConverter.convertStringToList(postDto.images()),
                postDto.category(),
                postDto.createdAt(),
                postDto.updatedAt(),
                postDto.userDto()
                        .id()
                        .equals(userDto.id()),
                userDto.nickname(),
                userDto.profileImage(),
                isLike
        );
    }
}
