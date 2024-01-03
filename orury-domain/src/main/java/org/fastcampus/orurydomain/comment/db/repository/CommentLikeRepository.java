package org.fastcampus.orurydomain.comment.db.repository;

import org.fastcampus.orurydomain.comment.db.model.CommentLike;
import org.fastcampus.orurydomain.comment.db.model.CommentLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikePK> {
    boolean existsCommentLikeByCommentLikePK_UserIdAndCommentLikePK_CommentId(Long userId, Long commentId);

    void deleteByCommentLikePK_CommentId(Long commentId);
}
