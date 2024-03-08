package org.orury.domain.comment.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.comment.domain.CommentReader;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.orury.domain.global.constants.NumberConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentReaderImpl implements CommentReader {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Optional<Comment> findCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public boolean existsCommentById(Long commentId) {
        return commentRepository.existsById(commentId);
    }

    @Override
    public boolean existsCommentLikeById(CommentLikePK commentLikePK) {
        return commentLikeRepository.existsById(commentLikePK);
    }

    @Override
    public boolean existsCommentLikeByUserIdAndCommentId(Long userId, Long commentId) {
        return commentLikeRepository.existsCommentLikeByCommentLikePK_UserIdAndCommentLikePK_CommentId(userId, commentId);
    }

    @Override
    public List<Comment> getCommentsByPostIdAndCursor(Long postId, Long cursor, PageRequest pageRequest) {
        List<Comment> parentComments = (cursor.equals(NumberConstants.LAST_CURSOR))
                ? Collections.emptyList()
                : commentRepository.findByPostIdAndParentIdAndIdGreaterThanOrderByIdAsc(postId, NumberConstants.PARENT_COMMENT, cursor, pageRequest);
        List<Comment> allComments = new LinkedList<>();
        parentComments.forEach(
                parentComment -> {
                    allComments.add(parentComment);
                    List<Comment> childComments = commentRepository.findByParentIdOrderByIdAsc(parentComment.getId());
                    if (!childComments.isEmpty()) allComments.addAll(childComments);
                }
        );
        return allComments;
    }

    @Override
    public List<Comment> getCommentsByUserIdAndCursor(Long userId, Long cursor, PageRequest pageRequest) {
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? commentRepository.findByUserIdOrderByIdDesc(userId, pageRequest)
                : commentRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageRequest);
    }
}
