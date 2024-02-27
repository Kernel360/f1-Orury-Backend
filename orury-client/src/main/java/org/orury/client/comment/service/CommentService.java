package org.orury.client.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.CommentErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.global.domain.ImageUtils;
import org.orury.domain.comment.db.model.Comment;
import org.orury.domain.comment.db.repository.CommentLikeRepository;
import org.orury.domain.comment.db.repository.CommentRepository;
import org.orury.domain.comment.dto.CommentDto;
import org.orury.domain.comment.dto.CommentLikeDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.client.post.infrastructure.PostRepository;
import org.orury.domain.post.dto.PostDto;
import org.orury.domain.user.dto.UserDto;
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
    private final ImageUtils imageUtils;

    @Transactional
    public void createComment(CommentDto commentDto) {
        commentRepository.save(commentDto.toEntity());
        postRepository.increaseCommentCount(commentDto.postDto().id());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentDtosByPost(PostDto postDto, Long cursor, Pageable pageable) {
        List<Comment> parentComments = (cursor.equals(NumberConstants.LAST_CURSOR))
                ? new ArrayList<>()
                : commentRepository.findByPostIdAndParentIdAndIdGreaterThanOrderByIdAsc(postDto.id(), NumberConstants.PARENT_COMMENT, cursor, pageable);
        List<Comment> allComments = new LinkedList<>();
        parentComments.forEach(
                comment -> {
                    allComments.add(comment);
                    List<Comment> childComments = commentRepository.findByParentIdOrderByIdAsc(comment.getId());
                    if (!childComments.isEmpty()) allComments.addAll(childComments);
                }
        );

        return allComments.stream()
                .map(comment -> {
                    String commentUserImage = imageUtils.getUserImageUrl(comment.getUser().getProfileImage());
                    return CommentDto.from(comment, commentUserImage);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentDtosByUserId(Long userId, Long cursor, Pageable pageable) {
        List<Comment> comments = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? commentRepository.findByUserIdOrderByIdDesc(userId, pageable)
                : commentRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);

        return comments.stream()
                .map(CommentDto::from)
                .toList();
    }

    @Transactional
    public void updateComment(CommentDto commentDto) {
        commentRepository.save(commentDto.toEntity());
    }

    @Transactional
    public void deleteComment(CommentDto commentDto) {
        Comment deletingComment = commentDto.toEntity().delete();
        commentRepository.save(deletingComment);
        commentLikeRepository.deleteByCommentLikePK_CommentId(deletingComment.getId());
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
        if (parentCommentId.equals(NumberConstants.PARENT_COMMENT)) return;
        if (!commentRepository.existsByIdAndParentId(parentCommentId, NumberConstants.PARENT_COMMENT)) {
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
