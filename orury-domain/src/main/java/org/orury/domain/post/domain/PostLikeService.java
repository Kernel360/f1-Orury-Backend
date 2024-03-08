package org.orury.domain.post.domain;

import org.orury.domain.post.domain.dto.PostLikeDto;

public interface PostLikeService {
    void createPostLike(PostLikeDto postLikeDto);

    void deletePostLike(PostLikeDto postLikeDto);

    boolean isLiked(Long userId, Long postId);
}
