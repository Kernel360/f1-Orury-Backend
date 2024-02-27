package org.orury.domain.post.domain;

import org.orury.domain.post.domain.db.Post;

public interface PostStore {
    void updateViewCount(Long id);

    void increaseCommentCount(Long id);

    void decreaseCommentCount(Long id);

    void increaseLikeCount(Long id);

    void decreaseLikeCount(Long id);

    void save(Post post);

    void delete(Post post);
}
