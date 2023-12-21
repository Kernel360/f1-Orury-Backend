package org.fastcampus.oruryapi.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.comment.converter.request.CommentCreateRequest;
import org.fastcampus.oruryapi.domain.comment.converter.request.CommentUpdateRequest;
import org.fastcampus.oruryapi.domain.comment.converter.response.CommentsWithCursorResponse;
import org.fastcampus.oruryapi.domain.comment.error.CommentErrorCode;
import org.fastcampus.oruryapi.domain.comment.service.CommentService;
import org.fastcampus.oruryapi.domain.comment.util.CommentMessage;
import org.fastcampus.oruryapi.global.constants.NumberConstants;
import org.fastcampus.oruryapi.global.error.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "댓글 정보를 받아, 댓글을 생성한다.")
    @PostMapping("/comment")
    public ApiResponse<Object> createPost(@RequestBody CommentCreateRequest request) {
        commentService.createComment(request, NumberConstants.USER_ID);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "댓글 조회", description = "게시글 id와 cursor값을 받아, '게시글 id와 cursor값에 따른 다음 댓글 목록'과 'cursor값(목록의 마지막 댓글 id / 조회된 댓글 없다면 -1L)'을 돌려준다.")
    @GetMapping("/comments/{postId}")
    public ApiResponse<CommentsWithCursorResponse> getCommentsByPostId(@PathVariable Long postId, @RequestParam Long cursor) {
        Long userId = NumberConstants.USER_ID;
        CommentsWithCursorResponse responses = commentService.getCommentsByPost(userId, postId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE));

        return ApiResponse.<CommentsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENTS_READ.getMessage())
                .data(responses)
                .build();
    }

    @Operation(summary = "댓글 수정", description = "댓글 정보를 받아, 댓글을 수정한다.")
    @PatchMapping("/comment")
    public ApiResponse<Object> updateComment(@RequestBody CommentUpdateRequest request) {
        Long userId = NumberConstants.USER_ID;
        if (!commentService.isValidate(userId, request.id()))
            throw new BusinessException(CommentErrorCode.UPDATE_FORBIDDEN);
        commentService.updateComment(request);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "댓글 삭제", description = "댓글 id를 받아, 댓글을 삭제한다.")
    @DeleteMapping("/comment/{id}")
    public ApiResponse<Object> deleteComment(@PathVariable Long id) {
        Long userId = NumberConstants.USER_ID;
        if (!commentService.isValidate(userId, id))
            throw new BusinessException(CommentErrorCode.DELETE_FORBIDDEN);
        commentService.deleteComment(id);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_DELETED.getMessage())
                .build();
    }
}
