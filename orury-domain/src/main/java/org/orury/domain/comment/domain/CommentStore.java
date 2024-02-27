package org.orury.domain.comment.domain;

import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLike;

public interface CommentStore {
    void createComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(Comment comment);

    void createCommentLike(CommentLike commentLike);

    void deleteCommentLike(CommentLike commentLike);

    void deleteCommentLikesByUserId(Long userId);
}
