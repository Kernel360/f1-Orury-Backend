package org.orury.domain.comment.infrastructure;

import org.orury.domain.comment.domain.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentIdAndIdGreaterThanOrderByIdAsc(Long postId, Long parentId, Long cursor, Pageable pageable);

    List<Comment> findByParentIdOrderByIdAsc(Long parentId);

    List<Comment> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Comment> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable);

    boolean existsByIdAndParentId(Long commentId, Long parentId);

    @Modifying
    @Query("UPDATE comment SET likeCount = likeCount + 1 WHERE id = :id")
    void increaseLikeCount(Long id);

    @Modifying
    @Query("UPDATE comment SET likeCount = likeCount - 1 WHERE id = :id")
    void decreaseLikeCount(Long id);
}
