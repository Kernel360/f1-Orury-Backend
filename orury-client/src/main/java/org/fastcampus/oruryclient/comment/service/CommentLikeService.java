package org.fastcampus.oruryclient.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.comment.error.CommentErrorCode;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.orurydomain.comment.dto.CommentLikeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createCommentLike(CommentLikeDto commentLikeDto) {
        commentRepository.findById(commentLikeDto.commentLikePK().getCommentId())
                        .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        if (commentLikeRepository.existsById(commentLikeDto.commentLikePK())) return;

        commentLikeRepository.save(commentLikeDto.toEntity());
        commentRepository.increaseLikeCount(commentLikeDto.commentLikePK().getCommentId());
    }

    @Transactional
    public void deleteCommentLike(CommentLikeDto commentLikeDto) {
        commentRepository.findById(commentLikeDto.commentLikePK().getCommentId())
                .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        if (!commentLikeRepository.existsById(commentLikeDto.commentLikePK())) return;

        commentLikeRepository.delete(commentLikeDto.toEntity());
        commentRepository.decreaseLikeCount(commentLikeDto.commentLikePK().getCommentId());
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long commentId) {
        return commentLikeRepository.existsCommentLikeByCommentLikePK_UserIdAndCommentLikePK_CommentId(userId, commentId);
    }
}
