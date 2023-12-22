package org.fastcampus.oruryapi.domain.comment.db.repository;

import org.fastcampus.oruryapi.domain.comment.db.model.CommentLike;
import org.fastcampus.oruryapi.domain.comment.db.model.CommentLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikePK> {
    int countByCommentLikePK_CommentId(Long commentId);

    boolean existsCommentLikeByCommentLikePK_CommentIdAndCommentLikePK_UserId(Long commentId, Long userId);

    Optional<CommentLike> findByCommentLikePK_UserIdAndCommentLikePK_CommentId(Long userId, Long commentId);
}
