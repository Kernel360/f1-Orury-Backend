package org.orury.domain.post.infrastructure;

import org.orury.domain.post.domain.db.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByIdDesc(int category, Pageable pageable);

    List<Post> findByCategoryAndIdLessThanOrderByIdDesc(int category, Long cursor, Pageable pageable);

    List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String titleSearchWord, String contentSearchWord, Pageable pageable);

    List<Post> findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(Long cursor1, String titleSearchWord, Long cursor2, String contentSearchWord, Pageable pageable);

    Page<Post> findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(int likeCount, LocalDateTime localDateTime, Pageable pageable);

    List<Post> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Post> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable);

    List<Post> findByUserId(Long userId);


    @Modifying
    @Query("UPDATE post SET viewCount = viewCount + 1 WHERE id = :id")
    void updateViewCount(Long id);

    @Modifying
    @Query("UPDATE post SET commentCount = commentCount + 1 WHERE id = :id")
    void increaseCommentCount(Long id);

    @Modifying
    @Query("UPDATE post SET commentCount = commentCount - 1 WHERE id = :id")
    void decreaseCommentCount(Long id);

    @Modifying
    @Query("UPDATE post SET likeCount = likeCount + 1 WHERE id = :id")
    void increaseLikeCount(Long id);

    @Modifying
    @Query("UPDATE post SET likeCount = likeCount - 1 WHERE id = :id")
    void decreaseLikeCount(Long id);
}