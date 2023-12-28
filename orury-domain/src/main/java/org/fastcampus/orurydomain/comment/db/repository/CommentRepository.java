package org.fastcampus.orurydomain.comment.db.repository;

import org.fastcampus.orurydomain.comment.db.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByPostId(Long postId);

    List<Comment> findByPost_IdAndParentIdAndIdGreaterThanOrderByIdAsc(Long postId, Long parentId, Long cursor, Pageable pageable);

    List<Comment> findByParentIdOrderByIdAsc(Long parentId);

    Comment findCommentById(Long id);
}
