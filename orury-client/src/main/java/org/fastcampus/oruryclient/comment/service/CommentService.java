package org.fastcampus.oruryclient.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.domain.comment.converter.dto.CommentDto;
import org.fastcampus.oruryclient.domain.comment.converter.request.CommentCreateRequest;
import org.fastcampus.oruryclient.domain.comment.converter.request.CommentUpdateRequest;
import org.fastcampus.oruryclient.domain.comment.converter.response.ChildCommentResponse;
import org.fastcampus.oruryclient.domain.comment.converter.response.CommentResponse;
import org.fastcampus.oruryclient.domain.comment.converter.response.CommentsWithCursorResponse;
import org.fastcampus.oruryclient.domain.comment.db.model.Comment;
import org.fastcampus.oruryclient.domain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.oruryclient.domain.comment.db.repository.CommentRepository;
import org.fastcampus.oruryclient.domain.comment.error.CommentErrorCode;
import org.fastcampus.oruryclient.domain.comment.util.CommentMessage;
import org.fastcampus.oruryclient.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryclient.domain.post.db.repository.PostRepository;
import org.fastcampus.oruryclient.domain.post.error.PostErrorCode;
import org.fastcampus.oruryclient.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryclient.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createComment(CommentCreateRequest request, Long userId) {
        UserDto userDto = UserDto.from(userRepository.findUserById(userId));
        PostDto postDto = PostDto.from(postRepository.findById(request.postId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND)));
        CommentDto commentDto = request.toDto(userDto, postDto);

        commentRepository.save(commentDto.toEntity());
    }

    @Transactional
    public void updateComment(CommentUpdateRequest request) {
        CommentDto commentDto = CommentDto.from(commentRepository.findCommentById(request.id()));

        commentRepository.save(request.toDto(commentDto).toEntity());
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findCommentById(id);
        CommentDto commentDto = CommentDto.of(comment.getId(),
                CommentMessage.COMMENT_DELETED.getMessage(),
                comment.getParentId(),
                PostDto.from(comment.getPost()),
                UserDto.from(comment.getUser()),
                NumberConstants.IS_DELETED,
                comment.getCreatedAt(),
                null
                );

        commentRepository.save(commentDto.toEntity());
    }

    public CommentsWithCursorResponse getCommentsByPost(Long userId, Long postId, Long cursor, Pageable pageable) {
        postRepository.findById(postId)
                .ifPresentOrElse(
                        post -> {},
                        () -> { throw new BusinessException(PostErrorCode.NOT_FOUND); }
                );
        List<Comment> comments = commentRepository.findByPost_IdAndParentIdAndIdGreaterThanOrderByIdAsc(postId, NumberConstants.PARENT_COMMENT, cursor, pageable);

        return getCommentsWithCursorResponse(comments, userId);
    }

    private CommentsWithCursorResponse getCommentsWithCursorResponse(List<Comment> comments, Long userId) {
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    CommentDto commentDto = CommentDto.from(comment);
                    int likeCount = commentLikeRepository.countByCommentLikePK_CommentId(comment.getId());
                    boolean isLike = commentLikeRepository.existsCommentLikeByCommentLikePK_CommentIdAndCommentLikePK_UserId(comment.getId(), userId);
                    List<ChildCommentResponse> childCommentResponses = getChildCommentResponses(userId, comment.getId());

                    return CommentResponse.of(commentDto, likeCount, isLike, childCommentResponses);
                }).toList();
        return CommentsWithCursorResponse.of(commentResponses);
    }

    private List<ChildCommentResponse> getChildCommentResponses(Long userId, Long commentId){
        List<Comment> childComments = commentRepository.findByParentIdOrderByIdAsc(commentId);

        return childComments.stream()
                .map(comment -> {
                    CommentDto commentDto = CommentDto.from(comment);
                    int likeCount = commentLikeRepository.countByCommentLikePK_CommentId(comment.getId());
                    boolean isLike = commentLikeRepository.existsCommentLikeByCommentLikePK_CommentIdAndCommentLikePK_UserId(comment.getId(), userId);

                    return ChildCommentResponse.of(commentDto, likeCount, isLike);
                }).toList();
    }

    public boolean isValidate(Long userId, Long commentId){
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND));
        var user = userRepository.findUserById(userId);

        return Objects.equals(user.getId(), comment.getUser().getId());
    }
}
