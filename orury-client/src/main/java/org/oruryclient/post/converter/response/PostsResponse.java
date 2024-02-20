package org.oruryclient.post.converter.response;

import org.orurydomain.post.dto.PostDto;

import java.time.LocalDateTime;

public record PostsResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int commentCount,
        int likeCount,
        String thumbnailImage,
        int category,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostsResponse of(PostDto postDto) {

        return new PostsResponse(
                postDto.id(),
                postDto.title(),
                postDto.content(),
                postDto.viewCount(),
                postDto.commentCount(),
                postDto.likeCount(),
//                (postDto.images().isEmpty()) ? null : ImageUrlConverter.convertStringToList(postDto.images()).get(0),
                (postDto.images()
                        .isEmpty()) ? null : postDto.images()
                        .get(0),
                postDto.category(),
                postDto.userDto()
                        .id(),
                postDto.userDto()
                        .nickname(),
                postDto.userDto()
                        .profileImage(),
                postDto.createdAt(),
                postDto.updatedAt()
        );
    }
}
