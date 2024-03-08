package org.orury.client.comment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.CommentErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.comment.domain.CommentReader;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentReader commentReader;
    private final CommentStore commentStore;

    @Override
    @Transactional
    public void createComment(CommentDto commentDto) {
        validateParentComment(commentDto.parentId());
        commentStore.createComment(commentDto.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentDtosByPost(PostDto postDto, Long cursor) {
        var pageRequest = PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE);
        var comments = commentReader.getCommentsByPostIdAndCursor(postDto.id(), cursor, pageRequest);
        return convertCommentsToCommentDtos(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentDtosByUserId(Long userId, Long cursor) {
        var pageRequest = PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE);
        var comments = commentReader.getCommentsByUserIdAndCursor(userId, cursor, pageRequest);
        return convertCommentsToCommentDtos(comments);
    }

    @Override
    @Transactional
    public void updateComment(CommentDto commentDto, Long userId) {
        validateCommentCreator(commentDto, userId);
        commentStore.updateComment(commentDto.toEntity());
    }

    @Override
    @Transactional
    public void deleteComment(CommentDto commentDto, Long userId) {
        validateCommentCreator(commentDto, userId);
        commentStore.deleteComment(commentDto.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentDtoById(Long id) {
        Comment comment = commentReader.findCommentById(id)
                .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        return CommentDto.from(comment);
    }

    @Override
    @Transactional
    public void createCommentLike(CommentLikeDto commentLikeDto) {
        validateComment(commentLikeDto.commentLikePK().getCommentId());
        if (commentReader.existsCommentLikeById(commentLikeDto.commentLikePK())) return;
        commentStore.createCommentLike(commentLikeDto.toEntity());
    }

    @Override
    @Transactional
    public void deleteCommentLike(CommentLikeDto commentLikeDto) {
        validateComment(commentLikeDto.commentLikePK().getCommentId());
        if (!commentReader.existsCommentLikeById(commentLikeDto.commentLikePK())) return;
        commentStore.deleteCommentLike(commentLikeDto.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long commentId) {
        return commentReader.existsCommentLikeByUserIdAndCommentId(userId, commentId);
    }

    private void validateCommentCreator(CommentDto commentDto, Long userId) {
        if (!Objects.equals(commentDto.userDto().id(), userId))
            throw new BusinessException(CommentErrorCode.FORBIDDEN);
    }

    private void validateComment(Long commentId) {
        var comment = commentReader.findCommentById(commentId)
                .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        if (comment.getDeleted() == NumberConstants.IS_DELETED)
            throw new BusinessException(CommentErrorCode.FORBIDDEN);
    }

    private void validateParentComment(Long parentCommentId) {
        if (parentCommentId.equals(NumberConstants.PARENT_COMMENT)) return;
        var parentComment = commentReader.findCommentById(parentCommentId)
                .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        if (!parentComment.getParentId().equals(NumberConstants.PARENT_COMMENT))
            throw new BusinessException(CommentErrorCode.BAD_REQUEST);
    }

    private List<CommentDto> convertCommentsToCommentDtos(List<Comment> comments) {
        return comments.stream().map(CommentDto::from).toList();
    }
}
