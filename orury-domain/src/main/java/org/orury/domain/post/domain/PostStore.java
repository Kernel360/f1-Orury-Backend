package org.orury.domain.post.domain;

import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;

public interface PostStore {
    void save(Post post);

    void delete(Post post);

    void save(PostLike postLike);

    void delete(PostLike postLike);

    void updateViewCount(Long id);

    void deletePostLikesByUserId(Long id);

    void deletePostsByUserId(Long id);
}
