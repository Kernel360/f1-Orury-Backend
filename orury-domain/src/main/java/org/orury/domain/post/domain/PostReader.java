package org.orury.domain.post.domain;

import org.orury.domain.post.domain.db.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostReader {
    List<Post> findByCategoryOrderByIdDesc(int category, Long cursor, Pageable pageable);

    List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String searchWord, Long cursor, Pageable pageable);

    List<Post> findByUserIdOrderByIdDesc(Long userId, Long cursor, Pageable pageable);

    Page<Post> findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(
            int likeCount,
            LocalDateTime localDateTime,
            Pageable pageable
    );

    Post findById(Long id);
}
