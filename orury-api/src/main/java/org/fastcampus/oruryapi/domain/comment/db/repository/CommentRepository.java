package org.fastcampus.oruryapi.domain.comment.db.repository;

import org.fastcampus.oruryapi.domain.comment.db.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByPostId(Long postId);
}
