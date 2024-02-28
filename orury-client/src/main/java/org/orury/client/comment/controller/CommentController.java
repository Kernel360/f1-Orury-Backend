package org.orury.client.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.comment.converter.message.CommentMessage;
import org.orury.client.comment.converter.request.CommentCreateRequest;
import org.orury.client.comment.converter.request.CommentUpdateRequest;
import org.orury.client.comment.converter.response.CommentResponse;
import org.orury.client.comment.converter.response.CommentsWithCursorResponse;
import org.orury.client.comment.service.CommentLikeService;
import org.orury.client.comment.service.CommentService;
import org.orury.client.post.service.PostService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.comment.dto.CommentDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.orury.domain.post.domain.dto.PostDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final UserService userService;
    private final PostService postService;

    @Operation(summary = "댓글 생성", description = "댓글 정보를 받아, 댓글을 생성한다.")
    @PostMapping
    public ApiResponse createComment(@RequestBody CommentCreateRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        PostDto postDto = postService.getPostDtoById(request.postId());
        commentService.validateParentComment(request.parentId());
        CommentDto commentDto = request.toDto(userDto, postDto);

        commentService.createComment(commentDto);

        return ApiResponse.of(CommentMessage.COMMENT_CREATED.getMessage());
    }

    @Operation(summary = "댓글 조회", description = "게시글 id와 cursor값을 받아, '게시글 id와 cursor값에 따른 다음 댓글 목록'과 'cursor값(목록의 마지막 댓글 id / 조회된 댓글 없다면 -1L)'을 돌려준다.")
    @GetMapping("/{postId}")
    public ApiResponse getCommentsByPostId(@PathVariable Long postId, @RequestParam Long cursor, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PostDto postDto = postService.getPostDtoById(postId);
        List<CommentDto> commentDtos = commentService.getCommentDtosByPost(postDto, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE));

        List<CommentResponse> commentResponses = commentDtos.stream()
                .map(commentDto -> {
                    boolean isLike = commentLikeService.isLiked(userPrincipal.id(), commentDto.id());
                    return CommentResponse.of(commentDto, userPrincipal.id(), isLike);
                })
                .toList();

        CommentsWithCursorResponse response = CommentsWithCursorResponse.of(commentResponses);
        return ApiResponse.of(CommentMessage.COMMENTS_READ.getMessage(), response);
    }

    @Operation(summary = "댓글 수정", description = "댓글 정보를 받아, 댓글을 수정한다.")
    @PatchMapping
    public ApiResponse updateComment(@RequestBody CommentUpdateRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        CommentDto commentDto = commentService.getCommentDtoById(request.id());
        commentService.isValidate(commentDto, userDto);

        CommentDto updatingCommentDto = request.toDto(commentDto);

        commentService.updateComment(updatingCommentDto);

        return ApiResponse.of(CommentMessage.COMMENT_UPDATED.getMessage());
    }

    @Operation(summary = "댓글 삭제", description = "댓글 id를 받아, 댓글을 삭제한다.")
    @DeleteMapping("/{id}")
    public ApiResponse deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        CommentDto commentDto = commentService.getCommentDtoById(id);
        commentService.isValidate(commentDto, userDto);

        commentService.deleteComment(commentDto);

        return ApiResponse.of(CommentMessage.COMMENT_DELETED.getMessage());
    }
}
