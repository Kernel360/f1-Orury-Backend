package org.fastcampus.oruryapi.domain.post.db.repository;

import org.fastcampus.oruryapi.domain.post.db.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByIdDesc(int category, Pageable pageable);

    List<Post> findByCategoryAndIdLessThanOrderByIdDesc(int category, Long cursor, Pageable pageable);

    List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String titleSearchWord, String contentSearchWord, Pageable pageable);

    List<Post> findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(Long cursor1, String titleSearchWord, Long cursor2, String contentSearchWord, Pageable pageable);

    @Modifying
    @Query("UPDATE post SET viewCount = viewCount + 1 WHERE id = :id")
    void updateViewCount(Long id);

    @Modifying
    @Query("UPDATE post SET likeCount = likeCount + 1 WHERE id = :id")
    void increaseLikeCount(Long id);

    @Modifying
    @Query("UPDATE post SET likeCount = likeCount - 1 WHERE id = :id")
    void decreaseLikeCount(Long id);
}