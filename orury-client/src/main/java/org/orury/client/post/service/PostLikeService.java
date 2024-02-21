package org.orury.client.post.service;

import org.orury.domain.post.dto.PostLikeDto;

public interface PostLikeService {
    void createPostLike(PostLikeDto postLikeDto);

    void deletePostLike(PostLikeDto postLikeDto);

    boolean isLiked(Long userId, Long postId);
}
