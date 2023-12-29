package org.fastcampus.oruryclient.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.comment.converter.request.CommentCreateRequest;
import org.fastcampus.oruryclient.comment.converter.request.CommentUpdateRequest;
import org.fastcampus.oruryclient.comment.converter.response.CommentResponse;
import org.fastcampus.oruryclient.comment.converter.response.CommentsWithCursorResponse;
import org.fastcampus.oruryclient.comment.service.CommentLikeService;
import org.fastcampus.oruryclient.comment.service.CommentService;
import org.fastcampus.oruryclient.comment.util.CommentMessage;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.post.service.PostService;
import org.fastcampus.oruryclient.user.service.UserService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final UserService userService;
    private final PostService postService;

    @Operation(summary = "댓글 생성", description = "댓글 정보를 받아, 댓글을 생성한다.")
    @PostMapping("/comment")
    public ApiResponse<Object> createComment(@RequestBody CommentCreateRequest request) {
        UserDto userDto = userService.getUserDtoById(NumberConstants.USER_ID);
        PostDto postDto = postService.getPostDtoById(request.postId());
        CommentDto commentDto = request.toDto(userDto, postDto);

        commentService.createComment(commentDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "댓글 조회", description = "게시글 id와 cursor값을 받아, '게시글 id와 cursor값에 따른 다음 댓글 목록'과 'cursor값(목록의 마지막 댓글 id / 조회된 댓글 없다면 -1L)'을 돌려준다.")
    @GetMapping("/comments/{postId}")
    public ApiResponse<CommentsWithCursorResponse> getCommentsByPostId(@PathVariable Long postId, @RequestParam Long cursor) {
        PostDto postDto = postService.getPostDtoById(postId);
        List<CommentDto> commentDtos = commentService.getCommentDtosByPost(postDto, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE));

        List<CommentResponse> commentResponses = commentDtos.stream()
                .map(commentDto -> {
                    boolean isLike = commentLikeService.isLiked(NumberConstants.USER_ID, commentDto.id());
                    return CommentResponse.of(commentDto, NumberConstants.USER_ID, isLike);
                }).toList();

        CommentsWithCursorResponse response = CommentsWithCursorResponse.of(commentResponses);

        return ApiResponse.<CommentsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENTS_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "댓글 수정", description = "댓글 정보를 받아, 댓글을 수정한다.")
    @PatchMapping("/comment")
    public ApiResponse<Object> updateComment(@RequestBody CommentUpdateRequest request) {
        UserDto userDto = userService.getUserDtoById(NumberConstants.USER_ID);
        CommentDto commentDto = commentService.getCommentDtoById(request.id());
        commentService.isValidate(commentDto, userDto);

        CommentDto updatingCommentDto = request.toDto(commentDto);

        commentService.updateComment(updatingCommentDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "댓글 삭제", description = "댓글 id를 받아, 댓글을 삭제한다.")
    @DeleteMapping("/comment/{id}")
    public ApiResponse<Object> deleteComment(@PathVariable Long id) {
        UserDto userDto = userService.getUserDtoById(NumberConstants.USER_ID);
        CommentDto commentDto = commentService.getCommentDtoById(id);
        commentService.isValidate(commentDto, userDto);

        commentService.deleteComment(commentDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_DELETED.getMessage())
                .build();
    }
}
