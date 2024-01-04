package org.fastcampus.oruryclient.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.comment.error.CommentErrorCode;
import org.fastcampus.oruryclient.comment.util.CommentMessage;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.global.logging.Logging;
import org.fastcampus.orurydomain.comment.db.model.Comment;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.comment.dto.CommentLikeDto;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;

    @Logging
    @Transactional
    public void createComment(CommentDto commentDto) {
        commentRepository.save(commentDto.toEntity());
        postRepository.increaseCommentCount(commentDto.postDto().id());
    }

    @Logging
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentDtosByPost(PostDto postDto, Long cursor, Pageable pageable) {
        List<Comment> parentComments = (cursor.equals(NumberConstants.LAST_CURSOR))
                ? new ArrayList<>()
                : commentRepository.findByPost_IdAndParentIdAndIdGreaterThanOrderByIdAsc(postDto.id(), NumberConstants.PARENT_COMMENT, cursor, pageable);
        List<Comment> allComments = new LinkedList<>();
        parentComments.forEach(
                comment -> {
                    allComments.add(comment);
                    List<Comment> childComments = commentRepository.findByParentIdOrderByIdAsc(comment.getId());
                    if (!childComments.isEmpty()) allComments.addAll(childComments);
                }
        );

        return allComments.stream()
                .map(CommentDto::from).toList();
    }

    @Logging
    @Transactional
    public void updateComment(CommentDto commentDto) {
        commentRepository.save(commentDto.toEntity());
    }

    @Logging
    @Transactional
    public void deleteComment(CommentDto commentDto) {
        CommentDto deletingCommentDto = CommentDto.of(
                commentDto.id(),
                CommentMessage.COMMENT_DELETED.getMessage(),
                commentDto.parentId(),
                0,
                commentDto.postDto(),
                commentDto.userDto(),
                NumberConstants.IS_DELETED,
                commentDto.createdAt(),
                null
        );
        commentRepository.save(deletingCommentDto.toEntity());
        commentLikeRepository.deleteByCommentLikePK_CommentId(deletingCommentDto.id());
        postRepository.decreaseCommentCount(commentDto.postDto().id());
    }

    public void isValidate(CommentDto commentDto, UserDto userDto) {
        if (!Objects.equals(commentDto.userDto().id(), userDto.id()))
            throw new BusinessException(CommentErrorCode.FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public void isValidate(CommentLikeDto commentLikeDto) {
        Comment comment = commentRepository.findById(commentLikeDto.commentLikePK().getCommentId())
                .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        if (comment.getDeleted() == NumberConstants.IS_DELETED)
            throw new BusinessException(CommentErrorCode.FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public void validateParentComment(Long parentCommentId) {
        if (!parentCommentId.equals(NumberConstants.PARENT_COMMENT)) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
            if (!parentComment.getParentId().equals(NumberConstants.PARENT_COMMENT))
                throw new BusinessException(CommentErrorCode.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentDtoById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        return CommentDto.from(comment);
    }
}
