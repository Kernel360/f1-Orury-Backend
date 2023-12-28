package org.fastcampus.oruryclient.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.comment.converter.request.CommentLikeRequest;
import org.fastcampus.orurydomain.comment.db.model.CommentLike;
import org.fastcampus.orurydomain.comment.db.model.CommentLikePK;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.oruryclient.comment.error.CommentErrorCode;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.global.error.code.UserErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentLikeService {
    private CommentLikeRepository commentLikeRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    public void createCommentLike(Long userId, CommentLikeRequest request) {
        isValidate(userId, request.commentId());
        CommentLike commentLike = CommentLike.of(CommentLikePK.of(userId, request.commentId()));

        commentLikeRepository.save(commentLike);
    }

    public void deleteCommentLike(Long userId, CommentLikeRequest request) {
        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentLikePK_UserIdAndCommentLikePK_CommentId(userId, request.commentId());
        if (commentLike.isEmpty()) return;

        commentLikeRepository.delete(commentLike.get());
    }

    public void isValidate(Long userId, Long commentId) {
        commentRepository.findById(commentId)
                .ifPresentOrElse(
                        comment -> {
                        },
                        () -> {
                            throw new BusinessException(CommentErrorCode.NOT_FOUND);
                        }
                );
        userRepository.findById(userId)
                .ifPresentOrElse(
                        user -> {
                        },
                        () -> {
                            throw new BusinessException(UserErrorCode.NOT_FOUND);
                        }

                );
    }
}
