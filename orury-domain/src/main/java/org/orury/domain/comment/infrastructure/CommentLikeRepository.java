package org.orury.domain.comment.infrastructure;

import org.orury.domain.comment.domain.entity.CommentLike;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikePK> {
    boolean existsCommentLikeByCommentLikePK_UserIdAndCommentLikePK_CommentId(Long userId, Long commentId);

    void deleteByCommentLikePK_CommentId(Long commentId);

    List<CommentLike> findByCommentLikePK_UserId(Long userId);
}
