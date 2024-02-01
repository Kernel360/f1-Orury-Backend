package org.fastcampus.oruryclient.comment.controller;

import org.fastcampus.oruryclient.comment.converter.message.CommentMessage;
import org.fastcampus.oruryclient.comment.converter.request.CommentCreateRequest;
import org.fastcampus.oruryclient.comment.converter.request.CommentUpdateRequest;
import org.fastcampus.oruryclient.comment.converter.response.CommentResponse;
import org.fastcampus.oruryclient.comment.converter.response.CommentsWithCursorResponse;
import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.oruryclient.config.WithUserPrincipal;
import org.fastcampus.orurycommon.error.code.CommentErrorCode;
import org.fastcampus.orurycommon.error.code.PostErrorCode;
import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Controller] 댓글 관련 테스트")
@WithUserPrincipal
class CommentControllerTest extends ControllerTest {

    @DisplayName("[POST] 유저 id, 게시글 id를 가지고 댓글 정보를 받아 댓글을 생성한다. - 성공")
    @Test
    void given_UserIdAndPostIdAndRequestComment_When_CreateComment_Then_Successfully() throws Exception {
        //given
        UserDto userDto = createUserDto();
        PostDto postDto = createPostDto();
        CommentCreateRequest request = createCommentCreateRequest();
        CommentDto commentDto = request.toDto(userDto, postDto);
        CommentMessage code = CommentMessage.COMMENT_CREATED;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(postService.getPostDtoById(any())).willReturn(postDto);

        //when & then
        mvc.perform(post("/api/v1/comments")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(commentService).should(times(1))
                .validateParentComment(any());
        then(commentService).should(times(1))
                .createComment(commentDto);
    }

    @DisplayName("[POST] 올바르지 않은 유저 id, 게시글 id를 가지고 댓글 정보를 받아 댓글을 생성시 예외 발생 - 실패")
    @Test
    void given_InvalidUserIdAndPostIdAndRequestComment_When_CreateComment_Then_NotFoundException() throws Exception {
        //given
        UserDto userDto = createUserDto();
        PostDto postDto = createPostDto();
        CommentCreateRequest request = createCommentCreateRequest();
        UserErrorCode code = UserErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(any())).willThrow(new BusinessException(code));
        given(postService.getPostDtoById(any())).willReturn(postDto);

        //when & then
        mvc.perform(post("/api/v1/comments")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(commentService).should(times(0))
                .validateParentComment(any());
        then(commentService).should(times(0))
                .createComment(any());
    }

    @DisplayName("[POST] 유저 id, 올바르지 않은 게시글 id를 가지고 댓글 정보를 받아 댓글을 생성시 예외 발생 - 실패")
    @Test
    void given_UserIdAndInvalidPostIdAndRequestComment_When_CreateComment_Then_NotFoundException() throws Exception {
        //given
        UserDto userDto = createUserDto();
        PostDto postDto = createPostDto();
        CommentCreateRequest request = createCommentCreateRequest();
        PostErrorCode code = PostErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(postService.getPostDtoById(any())).willThrow(new BusinessException(code));

        //when & then
        mvc.perform(post("/api/v1/comments")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(commentService).should(times(0))
                .validateParentComment(any());
        then(commentService).should(times(0))
                .createComment(any());
    }

    @DisplayName("[POST] 유저 id, 게시글 id, 올바르지 않은 부모 댓글 id를 가지고 댓글 정보를 받아 대댓글을 생성시 예외 발생 - 실패")
    @Test
    void given_UserIdAndPostIdAndInvalidParentCommentIdAndRequestComment_When_CreateComment_Then_NotFoundException() throws Exception {
        //given
        UserDto userDto = createUserDto();
        PostDto postDto = createPostDto();
        CommentCreateRequest request = createCommentCreateRequest();
        CommentErrorCode code = CommentErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(postService.getPostDtoById(any())).willReturn(postDto);
        willThrow(new BusinessException(code)).given(commentService)
                .validateParentComment(any());

        //when & then
        mvc.perform(post("/api/v1/comments")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(commentService).should(times(1))
                .validateParentComment(any());
        then(commentService).should(times(0))
                .createComment(any());
    }

    @DisplayName("[GET] 게시글 id, cursor값을 가지고 댓글 목록을 조회 한다. - 성공")
    @Test
    void given_PostIdAndCursor_When_GetCommentList_Then_ResponseCommentsAndCursor() throws Exception {
        //given
        Long cursor = 1L;
        PostDto postDto = createPostDto();
        List<CommentDto> commentDtos = List.of(createCommentDto(2L), createCommentDto(3L), createCommentDto(4L));
        List<CommentResponse> commentResponses = commentDtos.stream()
                .map(commentDto -> {
                    boolean isLike = true;
                    given(commentLikeService.isLiked(NumberConstants.USER_ID, commentDto.id())).willReturn(isLike);
                    return CommentResponse.of(commentDto, NumberConstants.USER_ID, isLike);
                })
                .toList();
        CommentMessage code = CommentMessage.COMMENTS_READ;
        CommentsWithCursorResponse response = CommentsWithCursorResponse.of(commentResponses);

        given(postService.getPostDtoById(1L)).willReturn(postDto);
        given(commentService.getCommentDtosByPost(
                postDto,
                cursor,
                PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE))
        ).willReturn(commentDtos);

        //when & then
        mvc.perform(get("/api/v1/comments/" + 1L)
                        .contentType(APPLICATION_JSON)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.comments[0].id").value(response.comments()
                        .get(0)
                        .id()))
                .andExpect(jsonPath("$.data.comments[0].content").value(response.comments()
                        .get(0)
                        .content()))
                .andExpect(jsonPath("$.data.cursor").value(response.cursor()))
        ;

        then(postService).should(times(1))
                .getPostDtoById(postDto.id());
        then(commentService).should(times(1))
                .getCommentDtosByPost(
                        postDto,
                        1L,
                        PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE)
                );
        for (int i = 1; i <= response.comments()
                .size(); i++)
            then(commentLikeService).should(times(1))
                    .isLiked(NumberConstants.USER_ID, (long) (i + 1));
    }

    @DisplayName("[GET] 게시글 id, cursor값을 가지고 댓글 목록을 조회 한다.(조회된 댓글 없는 경우)  - 성공")
    @Test
    void given_PostIdAndCursor_When_GetCommentList_Then_ResponseCommentsAndLastCursor() throws Exception {
        //given
        Long cursor = 1L;
        PostDto postDto = createPostDto();
        CommentMessage code = CommentMessage.COMMENTS_READ;
        CommentsWithCursorResponse response = CommentsWithCursorResponse.of(List.of());

        given(postService.getPostDtoById(1L)).willReturn(postDto);
        given(commentService.getCommentDtosByPost(
                postDto,
                cursor,
                PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE))
        ).willReturn(List.of());

        //when & then
        mvc.perform(get("/api/v1/comments/" + 1L)
                        .contentType(APPLICATION_JSON)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.comments.size()").value(0))
                .andExpect(jsonPath("$.data.cursor").value(-1))
        ;

        then(postService).should(times(1))
                .getPostDtoById(postDto.id());
        then(commentService).should(times(1))
                .getCommentDtosByPost(
                        postDto,
                        1L,
                        PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE)
                );
        then(commentLikeService).should(times(0))
                .isLiked(any(), any());
    }

    @DisplayName("[GET] 올바르지 않은 게시글 id, cursor값을 가지고 댓글 목록을 조회시 예외 발생 - 실패")
    @Test
    void given_InvalidPostIdAndCursor_When_GetCommentList_Then_NotFoundException() throws Exception {
        //given
        Long cursor = 1L;
        CommentErrorCode code = CommentErrorCode.NOT_FOUND;

        given(postService.getPostDtoById(1L)).willThrow(new BusinessException(code));
        given(commentService.getCommentDtosByPost(any(), any(), any())).willReturn(any());

        //when & then
        mvc.perform(get("/api/v1/comments/" + 1L)
                        .contentType(APPLICATION_JSON)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(postService).should(times(1))
                .getPostDtoById(1L);
        then(commentService).should(times(0))
                .getCommentDtosByPost(any(), any(), any());
        then(commentLikeService).should(times(0))
                .isLiked(any(), any());
    }

    @DisplayName("[PATCH] 유저 id, 댓글 id를 가지고 댓글 정보를 받아 댓글을 수정한다. - 성공")
    @Test
    void given_UserIdAndCommentIdAndRequestComment_When_UpdateComment_Then_Successfully() throws Exception {
        UserDto userDto = createUserDto();
        CommentDto commentDto = createCommentDto();
        CommentMessage code = CommentMessage.COMMENT_UPDATED;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(commentService.getCommentDtoById(any())).willReturn(commentDto);

        //when & then
        mvc.perform(patch("/api/v1/comments")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createCommentUpdateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(userService).should(times(1))
                .getUserDtoById(any());
        then(commentService).should(times(1))
                .getCommentDtoById(any());
        then(commentService).should(times(1))
                .isValidate(any(), any());
        then(commentService).should(times(1))
                .updateComment(any());
    }

    @DisplayName("[PATCH] 수정 권한이 없는 유저 id, 댓글 id를 가지고 댓글 정보를 받아 댓글 수정시 예외 처리 - 실패")
    @Test
    void given_NotAuthorizationUserIdAndCommentIdAndRequestComment_When_UpdateComment_Then_ForbiddenException() throws Exception {
        UserDto userDto = createUserDto();
        CommentDto commentDto = createCommentDto();
        CommentErrorCode code = CommentErrorCode.FORBIDDEN;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(commentService.getCommentDtoById(any())).willReturn(commentDto);
        willThrow(new BusinessException(code)).given(commentService)
                .isValidate(any(), any());

        //when & then
        mvc.perform(patch("/api/v1/comments")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createCommentUpdateRequest())))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(userService).should(times(1))
                .getUserDtoById(any());
        then(commentService).should(times(1))
                .getCommentDtoById(any());
        then(commentService).should(times(1))
                .isValidate(any(), any());
        then(commentService).should(times(0))
                .updateComment(any());
    }

    @DisplayName("[PATCH] 유저 id, 올바르지 않은 댓글 id를 가지고 댓글 정보를 받아 댓글 수정시 예외 처리 - 실패")
    @Test
    void given_UserIdAndInvalidCommentIdAndRequestComment_When_UpdateComment_Then_NotFoundException() throws Exception {
        UserDto userDto = createUserDto();
        CommentErrorCode code = CommentErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(commentService.getCommentDtoById(any())).willThrow(new BusinessException(code));

        //when & then
        mvc.perform(patch("/api/v1/comments")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createCommentUpdateRequest())))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(userService).should(times(1))
                .getUserDtoById(any());
        then(commentService).should(times(1))
                .getCommentDtoById(any());
        then(commentService).should(times(0))
                .isValidate(any(), any());
        then(commentService).should(times(0))
                .updateComment(any());
    }

    @DisplayName("[DELETE] 유저 id, 댓글 id를 가지고 댓글을 삭제한다. - 성공")
    @Test
    void given_UserIdAndCommentId_When_DeleteComment_Then_Successfully() throws Exception {
        //given
        UserDto userDto = createUserDto();
        CommentDto commentDto = createCommentDto();
        CommentMessage code = CommentMessage.COMMENT_DELETED;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(commentService.getCommentDtoById(any())).willReturn(commentDto);

        //when & then
        mvc.perform(delete("/api/v1/comments/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(userService).should(times(1))
                .getUserDtoById(any());
        then(commentService).should(times(1))
                .getCommentDtoById(any());
        then(commentService).should(times(1))
                .isValidate(any(), any());
        then(commentService).should(times(1))
                .deleteComment(any());
    }

    @DisplayName("[DELETE] 권한이 없는 유저 id, 댓글 id를 가지고 댓글 삭제시 예외 처리- 실패")
    @Test
    void given_NotAuthorizationUserIdAndCommentId_When_DeleteComment_Then_NotFoundException() throws Exception {
        //given
        UserDto userDto = createUserDto();
        CommentDto commentDto = createCommentDto();
        CommentErrorCode code = CommentErrorCode.FORBIDDEN;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(commentService.getCommentDtoById(any())).willReturn(commentDto);
        willThrow(new BusinessException(code)).given(commentService)
                .isValidate(commentDto, userDto);

        //when & then
        mvc.perform(delete("/api/v1/comments/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(userService).should(times(1))
                .getUserDtoById(any());
        then(commentService).should(times(1))
                .getCommentDtoById(any());
        then(commentService).should(times(1))
                .isValidate(any(), any());
        then(commentService).should(times(0))
                .deleteComment(any());
    }

    @DisplayName("[DELETE] 유저 id, 올바르지 않은 댓글 id를 가지고 댓글 삭제시 예외 처리- 실패")
    @Test
    void given_UserIdAndInvalidCommentId_When_DeleteComment_Then_NotFoundException() throws Exception {
        UserDto userDto = createUserDto();
        CommentErrorCode code = CommentErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(any())).willReturn(userDto);
        given(commentService.getCommentDtoById(any())).willThrow(new BusinessException(code));

        //when & then
        mvc.perform(delete("/api/v1/comments/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(userService).should(times(1))
                .getUserDtoById(any());
        then(commentService).should(times(1))
                .getCommentDtoById(any());
        then(commentService).should(times(0))
                .isValidate(any(), any());
        then(commentService).should(times(0))
                .deleteComment(any());
    }

    private CommentUpdateRequest createCommentUpdateRequest() {
        return CommentUpdateRequest.of(
                1L,
                "content"
        );
    }

    private CommentCreateRequest createCommentCreateRequest() {
        return CommentCreateRequest.of(
                "content",
                null,
                1L
        );
    }

    private CommentDto createCommentDto() {
        return CommentDto.of(
                1L,
                "content",
                null,
                0,
                createPostDto(),
                createUserDto(),
                0,
                null,
                null
        );
    }

    private CommentDto createCommentDto(Long id) {
        return CommentDto.of(
                id,
                "content",
                1L,
                0,
                createPostDto(),
                createUserDto(),
                0,
                null,
                null
        );
    }

    private PostDto createPostDto() {
        return PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                "",
                1,
                createUserDto(),
                null,
                null
        );
    }

    private UserDto createUserDto() {
        return UserDto.of(
                1L,
                "mail@mail",
                "name",
                "pw",
                1,
                1,
                LocalDate.now(),
                null,
                null,
                null,
                NumberConstants.IS_NOT_DELETED
        );
    }
}