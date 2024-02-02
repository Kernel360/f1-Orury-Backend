package org.fastcampus.oruryclient.post.service;

import org.fastcampus.orurydomain.post.dto.PostLikeDto;

public interface PostLikeService {
    void createPostLike(PostLikeDto postLikeDto);

    void deletePostLike(PostLikeDto postLikeDto);

    boolean isLiked(Long userId, Long postId);
}
